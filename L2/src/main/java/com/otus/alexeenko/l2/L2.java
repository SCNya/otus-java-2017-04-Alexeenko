package com.otus.alexeenko.l2;

import com.otus.alexeenko.l2.simulator.Simulator;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

//VM options -Xmx512m -Xms512m
public class L2 {

    private static final int numberOfItems = 100;

    public static void main(String[] args) {
        long size;

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        size = Simulator.getSize(() -> new String("Nice try!".toCharArray()));
        System.out.println("Size is " + size + " bytes");

        size = Simulator.getSize(() -> Integer.valueOf(1));
        System.out.println("Size is " + size + " bytes");

        size = Simulator.getSize(String::new);
        System.out.println("Size is " + size + " bytes");

        size = Simulator.getSize(() -> {
            ArrayList<Double> list = (ArrayList<Double>) DoubleStream
                    .iterate(1, n -> n + 1)
                    .map((n) -> n / Math.PI)
                    .limit(100)
                    .boxed()
                    .collect(Collectors.toList());
            list.trimToSize();
            return list;
        });

        System.out.println("Size is " + size + " bytes");
    }
}