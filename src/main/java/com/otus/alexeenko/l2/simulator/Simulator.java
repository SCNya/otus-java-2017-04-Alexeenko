package com.otus.alexeenko.l2.simulator;

import java.math.BigInteger;
import java.util.function.Supplier;

/**
 * Created by Vsevolod on 08/04/2017.
 */

public class Simulator {
    private static final int numberOfTests = 1000;
    private static final int numberOfItems = 1000;
    private final Runtime runtime;
    private BigInteger memoryConsumption;
    private Object[] store;
    private Supplier<Object> getItem;

    public Simulator() {
        runtime = Runtime.getRuntime();
    }

    @SuppressWarnings("unchecked")
    public long getSize(Class classType, Class parameterType, Object obj) {

        if (classType != String.class || parameterType != String.class || obj.getClass() != String.class) {
            getItem = new Supplier<Object>() {
                private final Class cType = classType;
                private final Class pType = parameterType;
                private final Object object = obj;

                @Override
                public Object get() {
                    try {
                        return cType.getConstructor(pType).newInstance(object); //initialize store by obj
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        } else {
            getItem = new Supplier<Object>() {
                private final Class cType = classType;
                private final Class pType = parameterType;
                private final Object object = obj;

                @Override
                public Object get() {
                    try {
                        return cType.getConstructor(pType).newInstance("".concat(((String) object))); //initialize store by String
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };

        }

        System.out.println("\nSizeOf... \nObj Type: " + classType + "\nConstructor Type: "
                + parameterType);

        return simulate();
    }

    public long getSize(Class classType) {

        getItem = new Supplier<Object>() {
            private final Class cType = classType;

            @Override
            public Object get() {
                try {
                    return cType.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };


        System.out.println("\nSizeOf... \nObj Type: " + classType + "\nConstructor Type: Default");

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
        System.gc();
        long memoryBeforeTest = runtime.freeMemory();

        this.store = new Object[numberOfItems];

        for (int i = 0; i < numberOfItems; i++) {
            store[i] = getItem.get(); //initialize store
        }

        System.gc();

        memoryConsumption = memoryConsumption.add(BigInteger.valueOf(memoryBeforeTest - runtime.freeMemory()));
    }
}
