package com.otus.alexeenko.l11.advance3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vsevolod on 30/07/2017.
 */
public class Bubble {
    private final int[] array;
    private final AtomicInteger queue;
    private final AtomicInteger awaitCounter;
    private final AtomicInteger length;
    private final ExecutorService executor;
    private boolean active;
    private int currentPosition;
    private int currentLength;
    private Bubble next;

    public Bubble(int[] array, AtomicInteger queue, AtomicInteger awaitCounter,
                  AtomicInteger length) {
        this.array = array;
        this.queue = queue;
        this.awaitCounter = awaitCounter;
        this.length = length;
        this.executor = Executors.newSingleThreadExecutor();
        this.active = true;
        this.currentPosition = 0;
        this.currentLength = 0;
        this.next = null;
    }

    public void start() {
        executor.execute(this::bubbling);
    }

    private void bubbling() {
        while (active) {
            int queueLength = queue.get();

            if (queueLength > 0) {
                updateLength();
                int size = getSize(queueLength);

                for (int i = 0; i < size; ++i) {
                    check(currentPosition);
                    ++currentPosition;
                }

                if (currentPosition - size == 0)
                    next.queue.addAndGet(size - 1);
                else
                    next.queue.addAndGet(size);

                updatePosition(size);
            }
        }
    }

    private int getSize(int queueLength) {
        if (queueLength > 10000)
            queueLength = 10000;

        if (currentLength - queueLength >= 0)
            return queueLength;
        else
            return currentLength;
    }

    private void updateLength() {
        if (currentLength == 0) {
            currentLength = getNewLength();
            currentPosition = 0;
            checkAwaitCounter();
        }
    }

    private void checkAwaitCounter() {
        awaitCounter.incrementAndGet();
    }

    private void updatePosition(int size) {
        queue.addAndGet(-size);
        currentLength -= size;
    }

    private int getNewLength() {
        return length.decrementAndGet();
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
