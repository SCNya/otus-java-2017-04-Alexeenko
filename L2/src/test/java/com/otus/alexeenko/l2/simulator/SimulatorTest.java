package com.otus.alexeenko.l2.simulator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;


/**
 * Created by Vsevolod on 09/04/2017.
 */
public class SimulatorTest {

    private static final int numberOfItems = 100;
    private static final byte INT = 4;  //int = INT byte
    private static final byte BYTE = 1;  //byte = 1 byte
    private final Simulator simulator = new Simulator();
    private long size;

    @Test
    public void getSizeObject() {
        size = simulator.getSize(Object.class);

        System.out.println("\nSize of Object about " + size + " bytes\n");

        assertTrue(size >= 8 && size <= 24); //16 bytes on my system
    }

    @Test
    public void getSizeString() {
        size = simulator.getSize(String.class, String.class, "Nice try!");

        System.out.println("\nSize of \"Nice try!\" String about " + size + " bytes\n");

        assertTrue(size >= 56 && size <= 72); //64 bytes on my system
    }

    @Test
    public void getSizeEmptyString() {
        size = simulator.getSize(String.class);

        System.out.println("\nEmpty String about " + size + " bytes\n");

        assertTrue(size >= 16 && size <= 32); //24 bytes on my system
    }

    @Test
    public void getSizeInteger() {
        size = simulator.getSize(Integer.class, int.class, 101010); //any int

        System.out.println("\nSize of Integer about " + size + " bytes\n");

        assertTrue(size >= 8 && size <= 24); //16 bytes on my system
    }

    //Capacity not transmitted in new the Collection
    @Test
    public void getSizeArrayListOfDoubleWithStartCapacity() {
        List<Double> list = new ArrayList<>(numberOfItems * 2);

        for (int i = 1; i <= numberOfItems; i++)
            list.add(i / Math.PI);

        size = simulator.getSize(ArrayList.class, Collection.class, list);

        System.out.println("\nSize of ArrayList with start capacity and filling (Double * " + numberOfItems * 2
                + ") about " + size + " bytes\n");

        assertTrue(size >= (INT * numberOfItems) &&
                size <= (INT * numberOfItems + (INT * numberOfItems / 4))); //440 bytes on my system
    }

    @Test
    public void getSizeArrayListOfDouble() {
        List<Double> list = new ArrayList<>();

        for (int i = 1; i <= numberOfItems; i++)
            list.add(i / Math.PI);

        size = simulator.getSize(ArrayList.class, Collection.class, list);

        System.out.println("\nSize of ArrayList without capacity and filling (Double * " + numberOfItems
                + ") about " + size + " bytes\n");

        assertTrue(size >= (INT * numberOfItems) &&
                size <= (INT * numberOfItems + (INT * numberOfItems / 4))); //440 bytes on my system
    }

    @Test
    public void getSizeEmptyArrayListOfIntegerWithoutCapacity() {
        List<Integer> list = new ArrayList<>(0);

        size = simulator.getSize(ArrayList.class, Collection.class, list);

        System.out.println("\nSize of ArrayList (Integer empty) " + "with capacity " +
                0 + " about " + size + " bytes\n");

        assertTrue(size >= 16 && size <= 32); //24 bytes on my system

    }

    @Test
    public void getSizeEmptyArrayListOfIntegerWithCapacity() {
        List<Integer> list = new ArrayList<>(numberOfItems);

        size = simulator.getSize(ArrayList.class, int.class, numberOfItems);

        System.out.println("\nSize of ArrayList (Integer empty) " + "with manual capacity " +
                numberOfItems + " per list about " + size + " bytes\n");

        assertTrue(size >= (INT * numberOfItems) &&
                size <= (INT * numberOfItems + (INT * numberOfItems / 4))); //440 bytes on my system
    }

    @Test
    public void getSizeMyTestClassWithTwoArguments() {
        size = simulator.getSize(MyTestClass.class, new Class[]{int.class, float.class}, new Object[]{1, 0.1f});

        System.out.println("\nSize of MyTestClass " + "with int array[" +
                numberOfItems + "] and two fields on stack about " + size + " bytes\n");

        assertTrue(size >= (INT * MyTestClass.arraySize) &&
                size <= (INT * MyTestClass.arraySize + (INT * MyTestClass.arraySize / 4))); //240 bytes on my system
    }

    @Test
    public void getSizeArrayOfByte() {
        size = simulator.getSize(byte.class, numberOfItems);

        System.out.println("\nSize of byte Array " + "[" +
                numberOfItems + "] about " + size + " bytes\n");

        assertTrue(size >= (BYTE * numberOfItems) &&
                size <= (BYTE * numberOfItems + (BYTE * numberOfItems / 4))); //120 bytes on my system
    }

}