package com.otus.alexeenko.l11.advance2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vsevolod on 29/07/2017.
 */
public class Bubble {
    private final int[] array;
    private final AtomicInteger queue;
    private final AtomicInteger awaitCounter;
    private final AtomicInteger length;
    private final ExecutorService executor;
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
        this.currentPosition = 0;
        this.currentLength = 0;
        this.next = null;
    }

    public void start() {
        executor.execute(this::bubbling);
    }

    private void bubbling() {
        try {
            while (true) {
                if (queue.get() > 0) {

                    if (isComplete()) break;

                    check(currentPosition);

                    if (currentPosition >= 1)
                        next.queue.incrementAndGet();

                    checkAwaitCounter();
                    updatePosition();
                } else
                    Thread.sleep(0, 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isComplete() {
        if (currentLength == 0) {
            currentLength = getNewLength();

            if (currentLength == 0)
                return true;
            else
                currentPosition = 0;
        }
        return false;
    }

    private void checkAwaitCounter() {
        if (currentPosition == 0)
            awaitCounter.incrementAndGet();
    }

    private void updatePosition() {
        queue.decrementAndGet();
        --currentLength;
        ++currentPosition;
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
        executor.shutdown();
    }
}
