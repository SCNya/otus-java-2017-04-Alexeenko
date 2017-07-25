package com.otus.alexeenko.l11.advance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vsevolod on 17/07/2017.
 */
public class Bubble {
    private final int[] array;
    private ExecutorService executor;
    private CountDownLatch tasks;
    private int length;
    private int currentPosition;
    private Bubble next;

    public Bubble(int[] array) {
        this.array = array;
        this.next = null;
    }

    public void init(int length, CountDownLatch tasks) {
        this.tasks = tasks;
        this.length = length;
        this.currentPosition = 0;
        if (next != null && (length - 1) > 0) {
            next.init(length - 1, tasks);
        }
    }

    public void bubbling(int size) {
        for (int i = 0; i < size; ++i) {
            check(currentPosition);

            if (next != null & currentPosition >= 1)
                executor.execute(() -> next.bubbling(1));

            ++currentPosition;
        }

        if (currentPosition == length) {
            tasks.countDown();
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
        executor = Executors.newSingleThreadExecutor();
        next = bubble;
        return next;
    }

    public void await() {
        try {
            tasks.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Bubble next() {
        return next;
    }

    public void shutdown() {
        executor.shutdown();
    }

    public boolean hasNext() {
        return next != null;
    }
}
