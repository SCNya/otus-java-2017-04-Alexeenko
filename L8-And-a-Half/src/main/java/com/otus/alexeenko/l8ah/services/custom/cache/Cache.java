package com.otus.alexeenko.l8ah.services.custom.cache;

/**
 * Created by Vsevolod on 05/07/2017.
 */
public interface Cache {
    Object get(Object key);
    void put(Object key, Object value);
    String getName();
    void clear();
    void dispose();
}
