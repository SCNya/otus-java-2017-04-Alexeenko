package com.otus.alexeenko.l3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vsevolod on 19/04/2017.
 */
public class MyArrayListTest {
    private static final int size = 99;
    
    private List<Integer> testList;
    private List<Integer> src;
    private List<Integer> dest;

    public MyArrayListTest() {
        testList = new ArrayList<>();
        src = new MyArrayList<>();
        dest = new MyArrayList<>();

        for (int i = 0; i < size; ++i) {
            src.add(i);
            dest.add(i);
            testList.add(i);
        }
    }

    @Test
    public void addAll() {
        Collections.addAll(src,101, 111, 100);
        Collections.addAll(testList,101, 111, 100);

        for (int i = 0; i < size + 3; ++i)
            assertTrue(src.get(i) == testList.get(i));
    }

    @Test
    public void copy() {
        List<Integer> anotherTestList = new ArrayList<>();
        testList.clear();
        
        for (int i = 0; i < size; ++i) {
            anotherTestList.add(i);
            testList.add((int) (Math.random() * 100));
        }
        
        Collections.copy(anotherTestList, testList);
        Collections.copy(dest, testList);

        for (int i = 0; i < size; ++i)
            assertTrue(dest.get(i) == anotherTestList.get(i));

        testList.clear();
        for (int i = 0; i < size; ++i)
            testList.add(i);
        Collections.copy(testList, dest);

        for (int i = 0; i < size; ++i)
            assertTrue(dest.get(i) == dest.get(i));
        
    }

    @Test
    public void sort() {
        int temp;

        for (int i = 0; i < size; ++i) {
            temp = (int) (Math.random() * 100);
            dest.add(temp);
            testList.add(temp);
        }

        Collections.sort(dest);
        Collections.sort(testList);

        for (int i = 0; i < size; ++i)
            assertTrue(dest.get(i) == testList.get(i));
    }
}