package com.otus.alexeenko.l4;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vsevolod on 25/04/2017.
 */
public class GCLogger {
    private static final Logger logger = Logger.getLogger("Log");
    private static final Timer timer = new Timer();
    private static final ReentrantLock locker = new ReentrantLock();
    private static int young;
    private static int youngTime;
    private static int old;
    private static int oldTime;


    private static void initLog() throws Exception {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tc %n%4$s: %5$s%6$s%n");  // pure log

        Path path = Paths.get("./logs");
        //if directory exists?
        if (!Files.exists(path))
            Files.createDirectories(path);

        // This block configure the logger with handler and formatter
        Handler fh = new FileHandler("./logs/Log.log", true);

        logger.addHandler(fh);

        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        logger.info("Start");
    }

    private static void initGCMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;

            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    locker.lock();
                    if (isYoungGC(info.getGcName())) {
                        young++;
                        youngTime += info.getGcInfo().getDuration();
                    } else {
                        old++;
                        oldTime += info.getGcInfo().getDuration();
                    }
                    locker.unlock();
                }
            };

            emitter.addNotificationListener(listener, null, null);
        }
    }

    public static boolean isYoungGC(String name) {
        Pattern p = Pattern.compile("Scavenge|Young|ParNew|Copy");
        Matcher m = p.matcher(name);
        return m.find();
    }

    public static void run() throws Exception {
        initLog();
        initGCMonitoring();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                locker.lock();
                logger.info("Number of calls young GC - " + young + " duration - " + youngTime + " ms" +
                        "\nNumber of calls old GC - " + old + " duration - " + oldTime + " ms");
                young = 0;
                youngTime = 0;
                old = 0;
                oldTime = 0;
                locker.unlock();
            }
        }, 60000, 60000); // one minute
    }
}