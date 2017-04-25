package com.otus.alexeenko.l3;

import java.util.Collection;

//VM options -Xmx512m -Xms512m
public class L3 {
    private static final int MEASURE_COUNT = 100;

    public static void main(String... args) {
        Collection<Integer> example = new MyArrayList<>();
        int min = 0;
        int max = 9_999_999;
        for (int i = min; i < max + 1; i++) {
            example.add(i);
        }

        findTheElement(() -> example.contains(max));
    }

    private static void findTheElement(Runnable runnable) {
        long startTime = System.nanoTime();
        for (int i = 0; i < MEASURE_COUNT; i++)
            runnable.run();
        long finishTime = System.nanoTime();
        long timeNs = (finishTime - startTime) / MEASURE_COUNT;
        System.out.println("Time spent: " + timeNs + "ns (" + timeNs / 1_000_000 + "ms)");
    }
}
