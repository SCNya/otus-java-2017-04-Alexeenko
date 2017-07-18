package com.otus.alexeenko.l11;

import com.otus.alexeenko.l11.advance.AdvanceBubblesSorter;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by Vsevolod on 17/07/2017.
 */
public class AdvanceBubblesSorterTest {
    @Test
    public void sort1() {
        int[] array1 = {0, 9, 5, 6, 7, 1, 3, 8, 2, 4};
        int[] array2 = array1.clone();

        BubbleSorter sorter = new AdvanceBubblesSorter(array1);

        sorter.sort();
        Arrays.sort(array2);

        assertArrayEquals(array1, array2);
    }

    @Test
    public void sort2() {
        int[] array1 = {10, 9, 5, 6, 7, 1, 33, 8, 2, 4};
        int[] array2 = array1.clone();

        BubbleSorter sorter = new AdvanceBubblesSorter(array1);

        sorter.sort();
        Arrays.sort(array2);

        assertArrayEquals(array1, array2);
    }

    @Test
    public void sort3() {
        int[] array1 = {};
        int[] array2 = array1.clone();

        BubbleSorter sorter = new AdvanceBubblesSorter(array1);

        sorter.sort();
        Arrays.sort(array2);

        assertArrayEquals(array1, array2);
    }

    @Test
    public void sort4() {
        int[] array1 = {1};
        int[] array2 = array1.clone();

        BubbleSorter sorter = new AdvanceBubblesSorter(array1);

        sorter.sort();
        Arrays.sort(array2);

        assertArrayEquals(array1, array2);
    }

    @Test
    public void sort5() {
        int[] array1 = {9, 9, 9};
        int[] array2 = array1.clone();

        BubbleSorter sorter = new AdvanceBubblesSorter(array1);

        sorter.sort();
        Arrays.sort(array2);

        assertArrayEquals(array1, array2);
    }

    @Test
    public void sort6() {
        int[] array1 = {0, 9, 5, 6, 7, 1, 3, 8, 2, 4, 10,
                9, 5, 6, 7, 1, 33, 8, 2, 4, 1, 9, 9, 9};
        int[] array2 = array1.clone();

        BubbleSorter sorter = new AdvanceBubblesSorter(array1);

        sorter.sort();
        Arrays.sort(array2);

        assertArrayEquals(array1, array2);
    }

    @Test
    public void sort7() {
        int[] array1 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] array2 = array1.clone();

        BubbleSorter sorter = new AdvanceBubblesSorter(array1);

        sorter.sort();
        Arrays.sort(array2);

        assertArrayEquals(array1, array2);
    }

    @Test
    public void sort8() {
        int[] array1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] array2 = array1.clone();

        BubbleSorter sorter = new AdvanceBubblesSorter(array1);

        sorter.sort();
        Arrays.sort(array2);

        assertArrayEquals(array1, array2);
    }

    @Test
    public void sort9() {
        int[] array1 = {6, 9, 5, 6, 7, 1, 3, 8, 2, 4};
        int[] array2 = array1.clone();

        BubbleSorter sorter = new AdvanceBubblesSorter(array1);

        sorter.sort();
        Arrays.sort(array2);

        assertArrayEquals(array1, array2);
    }
}