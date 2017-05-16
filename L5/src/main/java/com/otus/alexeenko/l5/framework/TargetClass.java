package com.otus.alexeenko.l5.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vsevolod on 15/05/2017.
 */
class TargetClass {
    private final Class<?> clasz;
    private final List<Method> beforeList;
    private final List<Method> testList;
    private final List<Method> afterList;

    TargetClass(Class<?> clasz) {
        this.clasz = clasz;
        this.beforeList = new ArrayList<>();
        this.testList = new ArrayList<>();
        this.afterList = new ArrayList<>();
    }

    Class<?> getClasz() {
        return clasz;
    }

    List<Method> getBeforeList() {
        return beforeList;
    }

    void addBefore(Method before) {
        this.beforeList.add(before);
    }

    List<Method> getTestList() {
        return testList;
    }

    void addTest(Method test) {
        this.testList.add(test);
    }

    List<Method> getAfterList() {
        return afterList;
    }

    void addAfter(Method after) {
        this.afterList.add(after);
    }
}
