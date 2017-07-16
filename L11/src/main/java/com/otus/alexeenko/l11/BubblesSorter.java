package com.otus.alexeenko.l11;

/**
 * Created by Vsevolod on 17/07/2017.
 */
public class BubblesSorter {
    private int[] array;
    private int availableThreads;
    private Thread[] threads;

    public BubblesSorter(int[] array) {
        this.array = array;
        availableThreads = Runtime.getRuntime().availableProcessors();
        threads = new Thread[availableThreads];
    }

    public void sort() {
        int position = array.length - 1;

        try {
            while (position > 0) {
                int numberOfThreads = getThreads(position);
                work(position, numberOfThreads);

                position -= numberOfThreads;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getThreads(int position) {
        if (position >= availableThreads)
            return availableThreads;
        else
            return position;
    }

    private void work(int position, int numberOfThreads) throws InterruptedException {
        for (int i = 0; i < numberOfThreads; ++i) {
            int bubblePosition = position - i;
            threads[i] = new Thread(() -> bubbling(bubblePosition));
            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; ++i) {
            threads[i].join();
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
