package com.otus.alexeenko.l8ah.services.custom.cache;

import java.lang.ref.SoftReference;
import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Vsevolod on 06/07/2017.
 */
public class CacheEngine implements Cache {
    private static final int DURATION_OF_MAINTENANCE = 2; //S

    private class Value {
        private final long timeStamp;
        private final SoftReference<Object> value;
        private long usageCounter;

        public Value(long timeStamp, Object obj) {
            this.usageCounter = 0;
            this.timeStamp = timeStamp;
            this.value = new SoftReference<>(obj);
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public Object getValue() {
            return value.get();
        }
    }

    private final String name;
    private final MemoryStoreEvictionPolicy policy;
    private final Duration expiry;
    private final int maxEntries;
    private final Map<Object, Value> cache;
    private final ScheduledExecutorService executorService;
    private final ReentrantLock lock;

    public CacheEngine(String name, MemoryStoreEvictionPolicy policy, Duration expire, int maxEntries) {
        this.lock = new ReentrantLock();
        this.name = name;
        this.policy = policy;
        this.expiry = expire;
        this.maxEntries = maxEntries;
        this.cache = new LinkedHashMap<>();
        this.executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate
                (this::check, 0, DURATION_OF_MAINTENANCE, TimeUnit.SECONDS);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object get(Object key) {
        lock.lock();
        Value value = cache.get(key);

        if (value != null) {
            if (!isExpiryTimeStamp(value.getTimeStamp())) {
                value.usageCounter++;
                lock.unlock();
                return value.getValue();
            }
        }

        lock.unlock();
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        lock.lock();
        if (cache.size() >= maxEntries && !cache.containsKey(key)) {
            if (policy == MemoryStoreEvictionPolicy.FIFO)
                removeFirst();
            else
                removeLeastRecentlyUsed();
        }

        cache.put(key, new Value(System.currentTimeMillis(), value));
        lock.unlock();
    }

    @Override
    public void clear() {
        lock.lock();
        cache.clear();
        lock.unlock();
    }

    @Override
    public void dispose() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private void removeFirst() {
        Iterator<Value> it = cache.values().iterator();
        if (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    private void removeLeastRecentlyUsed() {
        long max;
        long current;
        Iterator<Value> maxIt;
        Iterator<Value> it = cache.values().iterator();

        if (it.hasNext()) {
            max = it.next().usageCounter;
            maxIt = it;

            while (it.hasNext()) {
                current = it.next().usageCounter;

                if (max < current) {
                    max = current;
                    maxIt = it;
                }
            }
            maxIt.remove();
        }
    }

    private void check() {
        lock.lock();
        Iterator<Value> it = cache.values().iterator();

        while(it.hasNext()) {
            if (isExpiryTimeStamp(it.next().getTimeStamp()))
                it.remove();
            else
                break;
        }
        lock.unlock();
    }

    private boolean isExpiryTimeStamp(long timeStamp) {
        return (System.currentTimeMillis() - timeStamp >= expiry.toMillis());
    }
}