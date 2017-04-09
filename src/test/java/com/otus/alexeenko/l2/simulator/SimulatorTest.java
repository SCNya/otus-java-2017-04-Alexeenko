package com.otus.alexeenko.l2.simulator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Vsevolod on 09/04/2017.
 */
public class SimulatorTest {

    private static final int numberOfItems = 100;
    private final Simulator simulator;
    private long size;

    public SimulatorTest() {
        simulator = new Simulator();
    }

    @Test
    public void getSizeString() {
        size = simulator.getSize(String.class, String.class, "Nice try!");

        System.out.println("\nSize of \"Nice try!\" String about " + size + " bytes\n");

        assertTrue (size >= 60 && size <= 76); //68 bytes on my system
    }

    @Test
    public void getSizeEmptyString() {
        size = simulator.getSize(String.class);

        System.out.println("\nEmpty String about " + size + " bytes\n");

        assertTrue (size >= 20 && size <= 36); //28 bytes on my system
    }

    @Test
    public void getSizeInteger() {
        size = simulator.getSize(Integer.class, int.class, 101010); //any int

        System.out.println("\nSize of Integer about " + size + " bytes\n");

        assertTrue (size >= 12 && size <= 28); //20 bytes on my system
    }

    @Test
    public void getSizeArrayListOfDoubleWithCapacity() {
        List<Double> list = new ArrayList<>(numberOfItems);

        for (int i = 0; i < numberOfItems; i++)
            list.add(i / Math.PI);

        size = simulator.getSize(ArrayList.class, Collection.class, list);

        System.out.println("\nSize of ArrayList with capacity (Double * " + numberOfItems
                + ") about "  + size + " bytes\n");

        assertTrue (size >= (4 * numberOfItems) &&
                size <= (4 * numberOfItems + (4 * numberOfItems / 4))); //444 bytes on my system
    }

    @Test
    public void getSizeArrayListOfDoubleWithoutCapacity() {
        List<Double> list = new ArrayList<>();

        for (int i = 0; i < numberOfItems; i++)
            list.add(i / Math.PI);

        size = simulator.getSize(ArrayList.class, Collection.class, list);

        System.out.println("\nSize of ArrayList without capacity  (Double * " + numberOfItems
                + ") about "  + size + " bytes\n");

        assertTrue (size >= (4 * numberOfItems) &&
                size <= (4 * numberOfItems + (4 * numberOfItems / 4))); //444 bytes on my system
    }

    @Test
    public void getSizeEmptyArrayListOfIntegerWithoutCapacity() {
        List<Integer> list = new ArrayList<>();

        size = simulator.getSize(ArrayList.class, Collection.class, list);

        System.out.println("\nSize of ArrayList (Integer empty) " + "with capacity " +
                0 + " about "  + size + " bytes\n");

        assertTrue (size >= 20 && size <= 36); //28 bytes on my system

    }

    @Test
    public void getSizeEmptyArrayListOfIntegerWithCapacity() {
        List<Integer> list = new ArrayList<>(numberOfItems);

        size = simulator.getSize(ArrayList.class, Collection.class, list);

        System.out.println("\nSize of ArrayList (Integer empty) " + "with capacity " +
                numberOfItems + " about "  + size + " bytes\n");

        assertTrue (size >= 20 && size <= 36); //28 bytes on my system
    }

    @Test
    public void getSizeMyTestClassWithTwoArguments() {
        size = simulator.getSize(MyTestClass.class, new Class[]{int.class, float.class}, new Object[] {1, 0.1f});

        System.out.println("\nSize of MyTestClass " + "with int array[" +
                numberOfItems + "] and two fields about "  + size + " bytes\n");

        assertTrue (size >= (4 * MyTestClass.arraySize) &&
                size <= (4 * MyTestClass.arraySize + (4 * MyTestClass.arraySize / 4))); //244 bytes on my system
    }

}