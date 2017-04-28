package com.otus.alexeenko.l3;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vsevolod on 19/04/2017.
 */
public class MyArrayListTest {
    private static final int size = 9999;

    private final List<Integer> src = new MyArrayList<>();
    private final List<Integer> dest = new MyArrayList<>();

    @Before
    public void BeforeMyArrayListTest() {
        for (int i = 0; i < size; ++i) {
            src.add(i);
            dest.add(i);
        }

        Collections.shuffle(dest);
        Collections.shuffle(src);
    }

    @Test
    public void addAll() {
        Collections.addAll(src,101, 111, 100);
        Collections.addAll(dest,101, 111, 100);

        for (int i = size; i < size + 3; ++i)
            assertEquals(src.get(i), dest.get(i));
    }

    @Test
    public void copy() {
        Collections.copy(src, dest);
        for (int i = 0; i < size; ++i)
            assertEquals(dest.get(i), src.get(i));

        Collections.copy(dest, src);
        for (int i = 0; i < size; ++i)
            assertEquals(dest.get(i), src.get(i));
        
    }


    @Test
    public void sort() {
        Collections.sort(dest);
        Collections.sort(src);

        for (int i = 0; i < size; ++i)
            assertEquals(dest.get(i), src.get(i));
    }
}