package com.otus.alexeenko.l4;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class L4 {
    private static final long size = 100000L;

    public static void main(String[] args) {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        try {
            GCLogger.run();
            memoryLeak();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void memoryLeak() throws OutOfMemoryError, InterruptedException {
        long a = size;
        long b = size;
        long newSize;
        List<Object> list;

        while (true) {
            Thread.sleep(3000);
            newSize = a + b;
            a = b / 5L;
            b = newSize;
            list = new ArrayList<>();

            for (long i = 0L; i < newSize; ++i)
                list.add(new Object());
        }
    }
}
