package com.otus.alexeenko.l2.simulator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vsevolod on 09/04/2017.
 */

//VM options -Xmx512m -Xms512m
public class SimulatorTest {

    private static final int numberOfItems = 100;
    private static final byte INT = 4;  //int = INT byte
    private static final byte BYTE = 1;  //byte = 1 byte
    private long size;

    @Test
    public void getSizeObject() {
        size = Simulator.getSize(Object::new);

        System.out.println("\nSize of Object about " + size + " bytes\n");

        assertTrue(size >= 8 && size <= 24); //16 bytes on my system
    }

    @Test
    public void getSizeString() {
        size = Simulator.getSize(() -> new String("Nice try!".toCharArray()));

        System.out.println("\nSize of \"Nice try!\" String about " + size + " bytes\n");

        assertTrue(size >= 56 && size <= 72); //64 bytes on my system
    }

    @Test
    public void getSizeEmptyString() {
        size = Simulator.getSize(String::new);

        System.out.println("\nEmpty String about " + size + " bytes\n");

        assertTrue(size >= 16 && size <= 32); //24 bytes on my system
    }

    @Test
    public void getSizeInteger() {
        size = Simulator.getSize(() -> new Integer(101010)); //any int

        System.out.println("\nSize of Integer about " + size + " bytes\n");

        assertTrue(size >= 8 && size <= 24); //16 bytes on my system
    }

    //Capacity not transmitted in new the Collection
    @Test
    public void getSizeArrayListOfDoubleWithTrim() {

        size = Simulator.getSize(() -> {
            ArrayList<Double> list = (ArrayList<Double>) DoubleStream
                    .iterate(1, n -> n + 1)
                    .map((n) -> n / Math.PI)
                    .limit(numberOfItems)
                    .boxed()
                    .collect(Collectors.toList());
            list.trimToSize();
            return list;
        });

        System.out.println("\nSize of ArrayList with filling and trim (Double * " + numberOfItems
                + ") about " + size + " bytes\n");

        assertTrue(size >= 2800 &&
                size <= 3000); //2842 bytes on my system
    }

    @Test
    public void getSizeArrayListOfDoubleWithoutTrim() {

        size = Simulator.getSize(() -> DoubleStream
                .iterate(1, n -> n + 1)
                .map((n) -> n / Math.PI)
                .limit(numberOfItems)
                .boxed()
                .collect(Collectors.toList()));

        System.out.println("\nSize of ArrayList without trim. Filled (Double * " + numberOfItems
                + ") about " + size + " bytes\n");

        assertTrue(size >= 2800 &&
                size <= 3000); //2880 bytes on my system
    }

    @Test
    public void getSizeEmptyArrayListOfIntegerWithoutCapacity() {

        size = Simulator.getSize(ArrayList::new);

        System.out.println("\nSize of ArrayList (Integer empty) " + "with capacity " +
                0 + " about " + size + " bytes\n");

        assertTrue(size >= 16 && size <= 32); //24 bytes on my system

    }

    @Test
    public void getSizeEmptyArrayListOfIntegerWithCapacity() {

        size = Simulator.getSize(() -> new ArrayList<>(numberOfItems));

        System.out.println("\nSize of ArrayList (Integer empty) " + "with manual capacity " +
                numberOfItems + " per list about " + size + " bytes\n");

        assertTrue(size >= (INT * numberOfItems) &&
                size <= (INT * numberOfItems + (INT * numberOfItems / 4))); //440 bytes on my system
    }

    @Test
    public void getSizeMyTestClassWithTwoArguments() {
        size = Simulator.getSize(() -> new MyTestClass(1, 0.1f));

        System.out.println("\nSize of MyTestClass " + "with int array[" +
                numberOfItems + "] and two fields on stack about " + size + " bytes\n");

        assertTrue(size >= (INT * MyTestClass.arraySize) &&
                size <= (INT * MyTestClass.arraySize + (INT * MyTestClass.arraySize / 4))); //240 bytes on my system
    }

    @Test
    public void getSizeArrayOfByte() {
        size = Simulator.getSize(() -> (new byte[numberOfItems]));

        System.out.println("\nSize of byte Array " + "[" +
                numberOfItems + "] about " + size + " bytes\n");

        assertTrue(size >= (BYTE * numberOfItems) &&
                size <= (BYTE * numberOfItems + (BYTE * numberOfItems / 4))); //120 bytes on my system
    }
}