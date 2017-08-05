package com.otus.alexeenko.database.services.custom.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.MemoryUnit;
import net.sf.ehcache.management.ManagementService;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

/**
 * Created by Vsevolod on 12/07/2017.
 */
public class MyCache {
    private static final int LIFE_TIME_SEC = 10;

    private final CacheManager cacheManager;
    private static MyCache instance;

    private MyCache() {
        this.cacheManager = new CacheManager();
        registerMBeans();
    }

    public synchronized static MyCache getInstance() {
        if (instance == null) {
            instance = new MyCache();
        }

        return instance;
    }

    public Cache getCache(String name) {
        return cacheManager.getCache(name);
    }

    public void createCache(String name) {
        CacheConfiguration configuration = new CacheConfiguration();
        configuration.name(name)
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.FIFO)
                .timeToLiveSeconds(LIFE_TIME_SEC)
                .maxBytesLocalHeap(1, MemoryUnit.MEGABYTES);

        cacheManager.addCache(new Cache(configuration));
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    private void registerMBeans() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ManagementService.registerMBeans(cacheManager, mBeanServer, false, false,
                true, true);
    }

    public void shutdown() {
        cacheManager.shutdown();
    }
}
