package com.otus.alexeenko.l5.framework;

/**
 * Created by vsevolod on 16/05/2017.
 */
public class Assert {
    public static void assertTrue(boolean condition) {
        if (!condition)
            fail();
    }

    public static void assertNotNull(Object object) {
        assertTrue(object != null);
    }

    public static void fail() {
        throw new AssertionError();
    }
}
