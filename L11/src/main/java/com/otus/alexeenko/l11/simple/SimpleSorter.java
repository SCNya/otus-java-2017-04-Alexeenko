package com.otus.alexeenko.l11.simple;

import com.otus.alexeenko.l11.Sorter;

public class SimpleSorter implements Sorter {
    private final int[] array;

    public SimpleSorter(int[] array) {
        this.array = array;
    }

    @Override
    public void sort() {
        int position = array.length - 1;

        while (position > 0) {
            for (int j = 0; j < position; j++)
                if (array[j] > array[(j + 1)])
                    swap(array, j, (j + 1));

            --position;
        }
    }

    private void swap(int array[], int i, int j) {
        int temp = array[i];

        array[i] = array[j];
        array[j] = temp;
    }
}
