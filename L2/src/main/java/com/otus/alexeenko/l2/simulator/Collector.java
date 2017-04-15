package com.otus.alexeenko.l2.simulator;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Vsevolod on 14/04/2017.
 */
public class Collector {
    private static CountDownLatch lock;
    private static String gcName;

    private Collector() {
    }

    public static void collect() {
        if (lock == null)
            installLock();

        if (gcName.equals("G1 Young Generation") || gcName.equals("G1 Old Generation"))  //Java9 or not
            lock = new CountDownLatch(1); //for G1
        else
            lock = new CountDownLatch(2); //for nonG1
        gc();
    }

    private static void gc() {
        try {
            System.gc();
            lock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void installLock() {
        List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    if (info.getGcCause().equals("System.gc()")) {
                        lock.countDown();
                    }
                }
            };

            for (GarbageCollectorMXBean gcMxBean : gcbeans) {
                gcName = gcMxBean.getName();
                break;
            }

            emitter.addNotificationListener(listener, null, null);
        }
    }
}
