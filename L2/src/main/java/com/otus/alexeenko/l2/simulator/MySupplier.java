package com.otus.alexeenko.l2.simulator;

import java.lang.reflect.Array;

/**
 * Created by Vsevolod on 11/04/2017.
 */
abstract class MySupplier {
    protected final Class cType;
    protected final Class[] pType;
    protected final Object[] objects;

    public MySupplier(Class classType, Class[] parameterTypes, Object[] objs) {
        this.cType = classType;
        this.pType = parameterTypes;
        this.objects = objs;
    }

    public MySupplier(Class classType) {
        this(classType, null, null);
    }

    public abstract Object get() throws Exception;
}

final class ObjGetter extends MySupplier {

    public ObjGetter(Class classType, Class[] parameterTypes, Object[] objs) {
        super(classType, parameterTypes, objs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object get() throws Exception {
            return cType.getDeclaredConstructor(pType).newInstance(objects); //initialize store by obj
    }
}

final class StringGetter extends MySupplier {

    public StringGetter(Class classType, Class[] parameterTypes, Object[] objs) {
        super(classType, parameterTypes, objs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object get() throws Exception {
            return cType.getDeclaredConstructor(pType[0]).newInstance("".concat(((String) objects[0]))); //initialize store by String
    }
}

final class SimpleObjGetter extends MySupplier {

    public SimpleObjGetter(Class classType) {
        super(classType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object get() throws Exception {
            return cType.getDeclaredConstructor().newInstance();
    }
}

final class ArrayGetter extends MySupplier {
    private final int length;

    public ArrayGetter(Class classType, int arrayLength) {
        super(classType);
        length = arrayLength;
    }

    @Override
    public Object get() {
        return Array.newInstance(cType, length);
    }
}