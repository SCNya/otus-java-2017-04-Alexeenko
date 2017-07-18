package com.otus.alexeenko.l11.simple;

import com.otus.alexeenko.l11.BubbleSorter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vsevolod on 17/07/2017.
 */
public class SimpleBubblesSorter implements BubbleSorter {
    private final int[] array;
    private final int availableProcessors;
    private final Thread[] threads;
    private final AtomicInteger activeThreads;

    public SimpleBubblesSorter(int[] array) {
        this.array = array;
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
        this.threads = new Thread[availableProcessors];
        this.activeThreads = new AtomicInteger(0);
    }

    @Override
    public void sort() {
        int length = array.length - 1;
        int steps = array.length - 1;

        while (steps > 0) {
            work(length);
            await();
            --steps;
        }
        join();
    }

    private void work(int length) {
        Thread workThread = new Thread(() -> bubbling(length));
        activeThreads.incrementAndGet();
        workThread.start();
    }

    private void await() {
        try {
            while (true) {
                if (activeThreads.get() < availableProcessors) {
                    break;
                } else {
                    Thread.sleep(1);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void join() {
        try {
            while (true) {
                if (activeThreads.get() == 0) {
                    break;
                } else {
                    Thread.sleep(1);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void bubbling(int length) {
        for (int i = 0; i < length; ++i) {
            check(i);
        }
        activeThreads.decrementAndGet();
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
