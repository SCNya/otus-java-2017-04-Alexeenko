package com.otus.alexeenko.l8ah.services.custom.cache;

/**
 * Created by Vsevolod on 05/07/2017.
 */
public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    String getName();
    void clear();
    void dispose();
}
