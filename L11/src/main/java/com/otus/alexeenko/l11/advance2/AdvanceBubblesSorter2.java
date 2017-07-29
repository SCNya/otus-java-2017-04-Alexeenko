package com.otus.alexeenko.l11.advance2;

import com.otus.alexeenko.l11.Sorter;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vsevolod on 29/07/2017.
 */
public class AdvanceBubblesSorter2 implements Sorter {
    private final int[] array;
    private final Queue<Integer> queue;
    private final AtomicInteger counter;
    private final int availableProcessors;
    private final Bubble first;

    public AdvanceBubblesSorter2(int[] array) {
        this.array = array;
        this.queue = new ConcurrentLinkedQueue<>();
        this.counter = new AtomicInteger(0);
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
        this.first = new Bubble(this.array, queue, counter);
    }

    @Override
    public void sort() {
        if (array.length > 0) {
            try {
                create();
                work();
                shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void work() {
        for (int i = 0; i < array.length - 1; ++i) {
            queue.add(i);
        }

    }

    private void create() {
        Bubble bubble = first;
        bubble.start();

        for (int i = 0; i < availableProcessors - 1; ++i) {
            bubble = bubble.add(new Bubble(array, new ConcurrentLinkedQueue<>(), counter));
            bubble.start();
        }
        bubble.add(first);
    }

    private void shutdown() throws InterruptedException {
        while (counter.get() != array.length - 1)
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
