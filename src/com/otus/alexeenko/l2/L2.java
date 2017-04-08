package com.otus.alexeenko.l2;

import com.otus.alexeenko.l2.simulator.Simulator;

import java.lang.management.ManagementFactory;

//VM options -Xmx512m -Xms512m
public class L2 {

    public static void main(String[] args) {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        /*try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        Simulator<String> simulator = new Simulator<>();

        long size = simulator.getSize(new String("Nice try!"));

        System.out.println("Size is " + size + " byte");
    }
}
