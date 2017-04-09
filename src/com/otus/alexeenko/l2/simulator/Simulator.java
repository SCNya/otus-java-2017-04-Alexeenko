package com.otus.alexeenko.l2.simulator;

import java.math.BigInteger;

/**
 * Created by Vsevolod on 08/04/2017.
 */

public class Simulator {
    private static final int numberOfTests = 1000;
    private static final int numberOfItems = 1000;
    private final Runtime runtime;
    private long memoryBeforeTest;
    private BigInteger memoryConsumption;
    private Object[] store;

    public Simulator() {
        runtime = Runtime.getRuntime();
        memoryConsumption = new BigInteger("0");
    }

    public long getSize(Class classType, Class parameterType, Object obj) {

        System.out.println("\nSimulating... \nObj Type: " + classType + "\nConstructor Type: "
                + parameterType);

        memoryConsumption = BigInteger.valueOf(0);

        for (int i = 0; i < numberOfTests; i++) {
            //System.out.println("Test: " + i);
            test(classType, parameterType, obj);
            clean();
        }
        return memoryConsumption.divide(BigInteger.valueOf(numberOfTests)).longValue() / numberOfItems;
    }

    public long getSize(Class<String> classType, Class<String> parameterType, String obj) {

        System.out.println("\nSimulating... \nObj Type: " + classType + "\nConstructor Type: "
                + parameterType);

        memoryConsumption = BigInteger.valueOf(0);

        for (int i = 0; i < numberOfTests; i++) {
            //System.out.println("Test: " + i);
            test(classType, parameterType, obj);
            clean();
        }
        return memoryConsumption.divide(BigInteger.valueOf(numberOfTests)).longValue() / numberOfItems;
    }

    public long getSize(Class classType) {

        System.out.println("\nSimulating... \nObj Type: " + classType + "\nConstructor Type: Default");

        memoryConsumption = BigInteger.valueOf(0);

        for (int i = 0; i < numberOfTests; i++) {
            //System.out.println("Test: " + i);
            test(classType);
            clean();
        }
        return memoryConsumption.divide(BigInteger.valueOf(numberOfTests)).longValue() / numberOfItems;
    }

    private void clean() {
        for (int i = 0; i < numberOfItems; i++)
            store[i] = null;

        this.store = null;
    }

    //@SuppressWarnings("unchecked")
    private void test(Class classType, Class parameterType, Object obj) {
        System.gc();
        memoryBeforeTest = runtime.freeMemory();

        this.store = new Object[numberOfItems];

        for (int i = 0; i < numberOfItems; i++) {
            try {
                store[i] = classType.getConstructor(parameterType).newInstance(obj); //initialize store by obj
                //store[i] = classType.getConstructor(parameterType).newInstance(new String("".concat(((String) obj)))); //initialize store by obj
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.gc();

        memoryConsumption = memoryConsumption.add(BigInteger.valueOf(memoryBeforeTest - runtime.freeMemory()));
    }

    //@SuppressWarnings("unchecked")
    private void test(Class<String> classType, Class<String> parameterType, String obj) {
        System.gc();
        memoryBeforeTest = runtime.freeMemory();

        this.store = new Object[numberOfItems];

        for (int i = 0; i < numberOfItems; i++) {
            try {
                store[i] = classType.getConstructor(parameterType).newInstance(new String("".concat(((String) obj)))); //initialize store by obj
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.gc();

        memoryConsumption = memoryConsumption.add(BigInteger.valueOf(memoryBeforeTest - runtime.freeMemory()));
    }

    //@SuppressWarnings("unchecked")
    private void test(Class classType) {
        System.gc();
        memoryBeforeTest = runtime.freeMemory();

        this.store = new Object[numberOfItems];

        for (int i = 0; i < numberOfItems; i++) {
            try {
                store[i] = classType.newInstance(); //initialize by default constructor
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.gc();

        memoryConsumption = memoryConsumption.add(BigInteger.valueOf(memoryBeforeTest - runtime.freeMemory()));
    }
}
