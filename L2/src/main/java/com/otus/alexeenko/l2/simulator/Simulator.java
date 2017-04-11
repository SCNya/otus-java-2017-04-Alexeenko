package com.otus.alexeenko.l2.simulator;

import java.math.BigInteger;

/**
 * Created by Vsevolod on 08/04/2017.
 */

public class Simulator {
    private static final int numberOfTests = 1000;
    private static final int numberOfItems = 1000;
    private final Runtime runtime;
    private BigInteger memoryConsumption;
    private Object[] store;
    private MySupplier getItem;

    public Simulator() {
        runtime = Runtime.getRuntime();
    }

    public long getSize(Class classType, Class parameterType, Object obj) {
        return getSize(classType, new Class[]{parameterType}, new Object[]{obj});
    }

    @SuppressWarnings("unchecked")
    public long getSize(Class classType, Class[] parameterTypes, Object[] objs) {

        if (classType != String.class || parameterTypes[0] != String.class || objs[0].getClass() != String.class)
            getItem = new ObjGetter(classType, parameterTypes, objs);
        else
            getItem = new StringGetter(classType, parameterTypes, objs);

        System.out.println("\nSizeOf... \nObj Type: " + classType + "\nConstructor Types: ");

        for (Class parameterType : parameterTypes)
            System.out.println(parameterType);

        return simulate();
    }

    public long getSize(Class classType) {
        getItem = new SimpleObjGetter(classType);

        System.out.println("\nSizeOf... \nObj Type: " + classType + "\nConstructor Type: Default");

        return simulate();
    }

    public long getSize(Class<? extends Number> classType, int arrayLength) {
        getItem = new ArrayGetter(classType, arrayLength);

        System.out.println("\nSizeOf... \nArray Type: " + classType + "\nLength: " + arrayLength);

        return simulate();
    }

    private long simulate() {
        memoryConsumption = BigInteger.valueOf(0);

        for (int i = 0; i < numberOfTests; i++) {
            test();
            clean();
        }
        return memoryConsumption.divide(BigInteger.valueOf(numberOfTests)).longValue() / numberOfItems;
    }

    private void clean() {
        for (int i = 0; i < numberOfItems; i++)
            store[i] = null;

        this.store = null;
    }

    private void test() {
        this.store = new Object[numberOfItems];

        System.gc();
        long memoryBeforeTest = runtime.totalMemory() - runtime.freeMemory();

        for (int i = 0; i < numberOfItems; i++) {
            store[i] = getItem.get(); //initialize store
        }

        System.gc();

        memoryConsumption = memoryConsumption.add(BigInteger.valueOf
                (runtime.totalMemory() - runtime.freeMemory() - memoryBeforeTest));
    }
}