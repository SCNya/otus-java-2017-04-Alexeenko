package com.otus.alexeenko.l7;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Vsevolod on 30/05/2017.
 */

public class PrimitiveClass implements Serializable {

    private static final long serialVersionUID = -8925273805778027764L;

    private int one = 1;
    private final BigInteger bigInteger = BigInteger.valueOf(100500);
    private final double pi = Math.PI;
    private char c = 'c';
    private final String str = "str";
    private Integer integer = 111;
    private boolean is = true;
    private final Collection<Integer> list = new ArrayList<>();
    private final Object[] objects1 = {"hola", new Transient(), ""};
    private final Object[] objects2 = {new Transient(), new Transient(), new Transient()};
    private final Transient trObj = new Transient();
    private final int[] primitives = {1, 2, 3};
    private final Object nullNull = null;
    private transient String trString = "transient";
    private final Map<Integer, String> map = new HashMap<>();


    public PrimitiveClass() {
        list.add(9);
        list.add(9);
        list.add(9);

        map.put(1, "S1");
        map.put(2, "S2");
        map.put(3, "S3");
    }

    public void setOne(int one) {
        this.one = one;
    }

    public void setC(char c) {
        this.c = c;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public void setIs(boolean is) {
        this.is = is;
    }

    public void setTrString(String trString) {
        this.trString = trString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimitiveClass that = (PrimitiveClass) o;
        return one == that.one &&
                Double.compare(that.pi, pi) == 0 &&
                c == that.c &&
                is == that.is &&
                Objects.equals(bigInteger, that.bigInteger) &&
                Objects.equals(str, that.str) &&
                Objects.equals(integer, that.integer) &&
                Objects.equals(list, that.list) &&
                Arrays.equals(objects1, that.objects1) &&
                Arrays.equals(objects2, that.objects2) &&
                Objects.equals(trObj, that.trObj) &&
                Arrays.equals(primitives, that.primitives) &&
                Objects.equals(nullNull, that.nullNull) &&
                Objects.equals(trString, that.trString) &&
                Objects.equals(map, that.map);
    }

    @Override
    public String toString() {
        return "PrimitiveClass{" +
                "one=" + one +
                ", bigInteger=" + bigInteger +
                ", pi=" + pi +
                ", c=" + c +
                ", str='" + str + '\'' +
                ", integer=" + integer +
                ", is=" + is +
                ", list=" + list +
                ", objects1=" + Arrays.toString(objects1) +
                ", objects2=" + Arrays.toString(objects2) +
                ", trObj=" + trObj +
                ", primitives=" + Arrays.toString(primitives) +
                ", nullNull=" + nullNull +
                ", trString='" + trString + '\'' +
                ", map=" + map +
                '}';
    }
}