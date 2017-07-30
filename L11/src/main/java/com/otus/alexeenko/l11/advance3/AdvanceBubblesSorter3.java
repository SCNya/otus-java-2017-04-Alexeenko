package com.otus.alexeenko.l11.advance3;

import com.otus.alexeenko.l11.Sorter;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vsevolod on 30/07/2017.
 */
public class AdvanceBubblesSorter3 implements Sorter {
    private final int[] array;
    private final AtomicInteger length;
    private final AtomicInteger queue;
    private final AtomicInteger awaitCounter;
    private final int availableProcessors;
    private final Bubble first;

    public AdvanceBubblesSorter3(int[] array) {
        this.array = array;
        this.length = new AtomicInteger(this.array.length);
        this.queue = new AtomicInteger(0);
        this.awaitCounter = new AtomicInteger(0);
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
        this.first = new Bubble(this.array, queue, awaitCounter, length);
    }

    @Override
    public void sort() {
        if (array.length > 0) {
            try {
                create();
                work();
                shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void work() {
        queue.set(array.length - 1);
    }

    private void create() {
        Bubble bubble = first;
        bubble.start();

        for (int i = 0; i < availableProcessors - 1; ++i) {
            bubble = bubble.add(new Bubble(array, new AtomicInteger(0),
                    awaitCounter, length));
            bubble.start();
        }
        bubble.add(first);
    }

    private void shutdown() throws InterruptedException {
        while (awaitCounter.get() != array.length - 1)
            Thread.sleep(10);

        Bubble bubble = first;

        for (int i = 0; i < availableProcessors; ++i) {
            bubble.shutdown();
            bubble = bubble.next();
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
