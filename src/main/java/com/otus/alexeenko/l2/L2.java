package com.otus.alexeenko.l2;

import com.otus.alexeenko.l2.simulator.Simulator;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//VM options -Xmx512m -Xms512m
public class L2 {

    private static final int numberOfItems = 100;

    public static void main(String[] args) {
        long size;

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        Simulator simulator = new Simulator();

        size = simulator.getSize(String.class, String.class, "Nice try!");
        System.out.println("Size is " + size + " byte");

        size = simulator.getSize(Integer.class, int.class, 1);
        System.out.println("Size is " + size + " byte");

        size = simulator.getSize(String.class);
        System.out.println("Size is " + size + " byte");

        List<Double> list = new ArrayList<>(numberOfItems);

        for (int i = 0; i < numberOfItems; i++)
            list.add(i/Math.PI);

        size = simulator.getSize(ArrayList.class, Collection.class, list);
        System.out.println("Size is " + size + " byte");
    }
}
