package com.otus.alexeenko.l3;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vsevolod on 19/04/2017.
 */
public class MyArrayListTest {
    private List<Integer> src;
    private List<Integer> dest;

    public MyArrayListTest() {
        src = new MyArrayList<>();
        dest = new MyArrayList<>();

        for (int i = 0; i < 9; ++i) {
            src.add(i);
            dest.add(0);
        }
    }

    @Test
    public void addAll() {
        Collections.addAll(dest,1, 2, 3);
    }

    @Test
    public void copy() {
        Collections.copy(dest, src);
    }

    @Test
    public void sort() {
        for (int i = 0; i < 9; ++i) {
            dest.add((int) (Math.random() * 100));
        }
        dest.forEach(System.out::println);
        Collections.sort(dest);
        dest.forEach(System.out::println);
    }
}