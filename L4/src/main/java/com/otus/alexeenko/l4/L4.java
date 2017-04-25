package com.otus.alexeenko.l4;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class L4 {
    private static final int size = 1;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        Thread.sleep(10000);
        memoryLeak();
    }

    private static void memoryLeak() throws InterruptedException {
        long a = 100000;
        long b = 100000;
        long fibSize = 0;
        List<Object[]> list;

        while(true) {
            Thread.sleep(3000);
            fibSize = a + b;
            a = b;
            b = fibSize;
            list = new ArrayList<>();

            for (long i = 0L; i < fibSize/100L; ++i)
                list.add(new Object[size]);
        }
    }
}
