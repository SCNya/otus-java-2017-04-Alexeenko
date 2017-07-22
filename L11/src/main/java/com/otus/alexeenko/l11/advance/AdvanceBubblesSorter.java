package com.otus.alexeenko.l11.advance;

import com.otus.alexeenko.l11.Sorter;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vsevolod on 17/07/2017.
 */
public class AdvanceBubblesSorter implements Sorter {
    private final int[] array;
    private final int availableProcessors;
    private final ExecutorService executor;

    public AdvanceBubblesSorter(int[] array) {
        this.array = array;
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(availableProcessors - 1); // -main
    }

    @Override
    public void sort() {
        if (array.length > 0) {
            int length = array.length - 1;


            Bubble first = new Bubble(array, executor);
            Bubble bubble = first;

            for (int i = 0; i < availableProcessors - 1; ++i) {
                bubble = bubble.add(new Bubble(array, executor));
            }

            while (length > 0) {
                int size = getCurrentLength(length);
                first.init(length, new CountDownLatch(size));
                first.bubbling(length);
                length -= size;
                first.await();
            }
        }
        executor.shutdown();
    }

    private int getCurrentLength(int length) {
        if (length >= availableProcessors)
            return availableProcessors;
        else
            return length;
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
