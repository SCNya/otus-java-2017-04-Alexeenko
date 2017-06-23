package com.otus.alexeenko.l8.services.custom;

import java.util.Objects;

/**
 * Created by Vsevolod on 23/06/2017.
 */
public class Key {
    private final long id;
    private final Class clazz;

    public Key(long id, Class clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return id == key.id &&
                Objects.equals(clazz, key.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, id);
    }
}