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

    public AdvanceBubblesSorter(int[] array) {
        this.array = array;
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
    }

    @Override
    public void sort() {
        if (array.length > 0) {
            int length = array.length - 1;
            Bubble bubble = Init();

            while (length > 0) {
                int size = getCurrentLength(length);
                bubble.init(length, new CountDownLatch(size));
                bubble.bubbling(length);
                length -= size;
                bubble.await();
            }
            shutdown(bubble);
        }
    }

    private Bubble Init() {
        Bubble first = new Bubble(array);
        Bubble bubble = first;

        for (int i = 0; i < availableProcessors - 1; ++i) {
            bubble = bubble.add(new Bubble(array));
        }
        return first;
    }

    private void shutdown(Bubble bubble) {
        while (bubble.hasNext()) {
            bubble.shutdown();
            bubble = bubble.next();
        }
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
