package com.otus.alexeenko.l10.db;

import com.otus.alexeenko.database.services.DataBaseService;
import com.otus.alexeenko.database.services.custom.CustomService;
import com.otus.alexeenko.database.services.datasets.AddressDataSet;
import com.otus.alexeenko.database.services.datasets.PhoneDataSet;
import com.otus.alexeenko.database.services.datasets.UserDataSet;
import net.sf.ehcache.management.CacheConfigurationMBean;
import net.sf.ehcache.management.CacheStatisticsMBean;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vsevolod on 11/07/2017.
 */
public class DB {
    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private final static String CACHE_STATISTICS_NAME =
            "net.sf.ehcache:CacheManager=__DEFAULT__,name=userCache,type=CacheStatistics";
    private final static String CACHE_CONFIGURATION_NAME =
            "net.sf.ehcache:CacheManager=__DEFAULT__,name=userCache,type=CacheConfiguration";

    private static DB instance;
    private final DataBaseService db;
    private final MBeanServer mbs;
    private final ExecutorService thread;

    private DB() {
        this.db = new CustomService();
        this.mbs = ManagementFactory.getPlatformMBeanServer();
        this.thread = Executors.newSingleThreadExecutor();
    }

    public synchronized static DB getInstance() {
        if (instance == null) {
            instance = new DB();
        }

        return instance;
    }

    public void run() {
        List<PhoneDataSet> phones = Arrays.asList(new PhoneDataSet(1L, 911, "1"),
                new PhoneDataSet(2L, 921, "2"));

        UserDataSet dataSet1 = new UserDataSet(1L, "First", 22,
                phones, new AddressDataSet(1L, "Kings Row", 90));

        UserDataSet dataSet2 = new UserDataSet(2L, "Second", 17,
                phones, new AddressDataSet(2L, "Dorado", 200));

        thread.execute(() -> work(dataSet1, dataSet2));
    }

    private void work(UserDataSet dataSet1, UserDataSet dataSet2) {
        try {
            db.start();

            db.save(dataSet1);

            db.save(dataSet2);

            while (true) {
                for (int i = 0; i < Integer.MAX_VALUE; ++i) {
                    if (i % 5 == 3)
                        db.load(2L, UserDataSet.class);
                    else
                        db.load(1L, UserDataSet.class);

                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException interrupt) {
            //Just Exit
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CacheStatisticsMBean getCacheCacheStatisticsMBean() {
        CacheStatisticsMBean cacheStatisticsMBean = null;
        ObjectName cacheStatisticsObjectName;

        try {
            cacheStatisticsObjectName = new ObjectName(CACHE_STATISTICS_NAME);
            cacheStatisticsMBean = JMX.newMBeanProxy(mbs, cacheStatisticsObjectName,
                    CacheStatisticsMBean.class, true);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }

        return cacheStatisticsMBean;
    }

    public CacheConfigurationMBean getCacheConfigurationMBean() {
        CacheConfigurationMBean cacheConfigurationMBean = null;
        ObjectName cacheConfigurationObjectName;

        try {
            cacheConfigurationObjectName = new ObjectName(CACHE_CONFIGURATION_NAME);
            cacheConfigurationMBean = JMX.newMBeanProxy(mbs, cacheConfigurationObjectName,
                    CacheConfigurationMBean.class, true);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        return cacheConfigurationMBean;
    }

    public synchronized void dispose() {
        thread.shutdownNow();
        db.dispose();
    }
}