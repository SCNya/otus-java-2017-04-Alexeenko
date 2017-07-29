package com.otus.alexeenko.l11.advance2;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vsevolod on 29/07/2017.
 */
public class Bubble {
    private final int[] array;
    private final Queue<Integer> queue;
    private final AtomicInteger counter;
    private final ExecutorService executor;
    private boolean active;
    private Bubble next;

    public Bubble(int[] array, Queue<Integer> queue, AtomicInteger counter) {
        this.array = array;
        this.queue = queue;
        this.counter = counter;
        this.executor = Executors.newSingleThreadExecutor();
        this.active = true;
        this.next = null;
    }

    public void start() {
        executor.execute(this::bubbling);
    }

    private void bubbling() {
        try {
            while (active) {
                Integer position = queue.poll();
                if (position != null) {
                    check(position);

                    if (position == 0)
                        counter.incrementAndGet();

                    if (position >= 1) {
                        Integer nextPosition = position - 1;
                        next.queue.add(nextPosition);
                    }
                } else
                    Thread.sleep(0, 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void check(int i) {
        int j = i + 1;

        if (array[i] > array[j])
            swap(i, j);
    }

    private void swap(int i, int j) {
        int temp = array[j];

        array[j] = array[i];
        array[i] = temp;
    }

    public Bubble add(Bubble bubble) {
        next = bubble;
        return next;
    }

    public Bubble next() {
        return next;
    }

    public void shutdown() {
        active = false;
        executor.shutdown();
    }
}
