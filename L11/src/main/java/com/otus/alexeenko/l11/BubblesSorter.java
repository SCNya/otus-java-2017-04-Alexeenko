package com.otus.alexeenko.l11;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vsevolod on 17/07/2017.
 */
public class BubblesSorter {
    private final int[] array;
    private final int availableThreads;
    private final ExecutorService executor;

    public BubblesSorter(int[] array) {
        this.array = array;
        this.availableThreads = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(availableThreads);
    }

    public void sort() {
        int position = array.length - 1;

            while (position > 0) {
                int numberOfThreads = getThreads(position);
                work(position, numberOfThreads);

                position -= numberOfThreads;
            }
        executor.shutdown();
    }

    private int getThreads(int position) {
        if (position >= availableThreads)
            return availableThreads;
        else
            return position;
    }

    private void work(int position, int numberOfThreads) {
        for (int i = 0; i < numberOfThreads; ++i) {
            int currentPosition = position - i;
            executor.submit(() -> bubbling(currentPosition));
        }
    }

    private void bubbling(int position) {
        for (int i = 0; i < position; i++)
            check(i);
    }

    private synchronized void check(int i) {
        int j = i + 1;

        if (array[i] > array[j])
            swap(i, j);
    }

    private void swap(int i, int j) {
        int temp = array[j];

        array[j] = array[i];
        array[i] = temp;
    }
}
