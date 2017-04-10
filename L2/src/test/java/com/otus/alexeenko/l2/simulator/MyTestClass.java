package com.otus.alexeenko.l2.simulator;

import java.util.Arrays;

/**
 * Created by Vsevolod on 09/04/2017.
 */
public class MyTestClass {
    public static final int arraySize = 50;
    private int a;
    private float b;
    private int[] array;

    public MyTestClass(int a, float b) {
        this.a = a;
        this.b = b;
        this.array = new int[arraySize];
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyTestClass that = (MyTestClass) o;

        if (a != that.a) return false;
        if (Float.compare(that.b, b) != 0) return false;
        return Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = a;
        result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }
}
