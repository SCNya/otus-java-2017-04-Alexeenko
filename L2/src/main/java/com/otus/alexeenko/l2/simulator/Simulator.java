package com.otus.alexeenko.l2.simulator;

import java.math.BigInteger;
import java.util.function.Supplier;

/**
 * Created by Vsevolod on 08/04/2017.
 */

public class Simulator {
    private static final int numberOfTests = 1000;
    private static final int numberOfItems = 1000;
    private static final Runtime runtime = Runtime.getRuntime();
    private static final Object[] store = new Object[numberOfItems];
    private static BigInteger memoryConsumption;
    private static Supplier<Object> getItem;

    private Simulator() {
    }

    public static long getSize(Supplier<Object> getter) {
        getItem = getter;
        System.out.println("\nSizeOf... \nObj Type: " + getter.get().getClass());

        return simulate();
    }

    private static long simulate() {
        memoryConsumption = BigInteger.valueOf(0);

        for (int i = 0; i < numberOfTests; i++) {
            test();
            clean();
        }

        return memoryConsumption.divide(BigInteger.valueOf(numberOfTests)).longValue() / numberOfItems;
    }

    private static void clean() {
        for (int i = 0; i < numberOfItems; i++)
            store[i] = null;
    }

    private static void test() {
        Collector.collect();

        long memoryBeforeTest = runtime.totalMemory() - runtime.freeMemory();

        for (int i = 0; i < numberOfItems; i++)
            store[i] = getItem.get(); //initialize store

        Collector.collect();

        memoryConsumption = memoryConsumption.add(BigInteger.valueOf
                (runtime.totalMemory() - runtime.freeMemory() - memoryBeforeTest));

    }
}