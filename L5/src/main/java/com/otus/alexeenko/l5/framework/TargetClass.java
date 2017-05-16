package com.otus.alexeenko.l5.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vsevolod on 15/05/2017.
 */
public class TargetClass {
    private final Class<?> clasz;
    private final List<Method> beforeList;
    private final List<Method> testList;
    private final List<Method> afterList;

    public TargetClass(Class<?> clasz) {
        this.clasz = clasz;
        this.beforeList = new ArrayList<>();
        this.testList = new ArrayList<>();
        this.afterList = new ArrayList<>();
    }

    public Class<?> getClasz() {
        return clasz;
    }

    public List<Method> getBeforeList() {
        return beforeList;
    }

    public void addBefore(Method before) {
        this.beforeList.add(before);
    }

    public List<Method> getTestList() {
        return testList;
    }

    public void addTest(Method test) {
        this.testList.add(test);
    }

    public List<Method> getAfterList() {
        return afterList;
    }

    public void addAfter(Method after) {
        this.afterList.add(after);
    }
}
