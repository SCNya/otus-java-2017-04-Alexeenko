package com.otus.alexeenko.l2.simulator;

import java.lang.reflect.Array;
import java.math.BigInteger;

/**
 * Created by Vsevolod on 08/04/2017.
 */

public class Simulator<T> {
    private static final int numberOfTests = 1000;
    private static final int numberOfItems = 1000;
    private final Runtime runtime;
    private long memoryBeforeTest;
    private BigInteger resultsOfTest;
    private T[] store;

    public Simulator() {
        runtime = Runtime.getRuntime();
        resultsOfTest = new BigInteger("0");
    }

    public long getSize(T obj) {

        System.out.println("Simulate");

        for (int i = 0; i < numberOfTests; i++) {
            System.out.println("Test: " + i);
            test(obj);
            clean();
        }
        return resultsOfTest.divide(BigInteger.valueOf(numberOfTests)).longValue() / numberOfItems;
    }

    private void clean() {
        for (int i = 0; i < numberOfItems; i++)
            store[i] = null;

        this.store = null;
    }

    @SuppressWarnings("unchecked")
    private void test(T obj) {
        System.gc();
        memoryBeforeTest = runtime.freeMemory();

        this.store = (T[]) Array.newInstance(obj.getClass(), numberOfItems);

        for (int i = 0; i < numberOfItems; i++) {
            try {
                store[i] = (T) obj.getClass().getConstructor(obj.getClass()).newInstance(obj); //initialize store by obj
            }
            catch (IllegalArgumentException e) {
                try {
                    store[i] = (T) obj.getClass().newInstance(); //initialize by default constructor
                } catch (InstantiationException iae) {
                    iae.printStackTrace();
                } catch (IllegalAccessException iae) {
                    iae.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.gc();

        resultsOfTest = resultsOfTest.add(BigInteger.valueOf(memoryBeforeTest -  runtime.freeMemory()));
    }
}
