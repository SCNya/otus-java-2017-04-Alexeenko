package com.otus.alexeenko.l3;

import java.util.*;

/**
 * Created by Vsevolod on 18/04/2017.
 */

public class MyArrayList<T> implements List<T>, RandomAccess, Cloneable, java.io.Serializable {

    private static final long serialVersionUID = -1284613836096340960L;

    private static final int DEFAULT_SIZE = 16;

    private int size;

    private int capacity;

    private Object array[];

    public MyArrayList() {
        this.array = new Object[DEFAULT_SIZE];
        capacity = DEFAULT_SIZE;
    }

    public MyArrayList(int capacity) {
        this.array = new Object[capacity];
        this.capacity = capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) > -1;
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] returnArray = new Object[size];
        System.arraycopy(array, 0, returnArray, 0, size);
        return returnArray;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T1[]) Arrays.copyOf(array, size, a.getClass());
        System.arraycopy(array, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(T t) {
        checkCapacity(1);
        array[size++] = t;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index;

        if ((index = indexOf(o)) > -1) {
            internalRemove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        checkCapacity(size + numNew);  // Change capacity
        System.arraycopy(a, 0, array, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        rangeCheck(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        checkCapacity(size + numNew);  // Change capacity

        int different = size - index;
        if (different > 0)
            System.arraycopy(array, index, array, index + numNew,
                    different);

        System.arraycopy(a, 0, array, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        capacity = DEFAULT_SIZE;
        size = 0;

        array = new Object[DEFAULT_SIZE];

        for (int i = 0; i < size; i++)
            array[i] = null;

        capacity += size;
        size = 0;
    }

    public void clearWithoutResize() {
        for (int i = 0; i < size; i++)
            array[i] = null;

        capacity += size;
        size = 0;
    }

    @Override
    public T get(int index) {
        rangeCheck(index);
        return getElement(index);
    }

    @Override
    public T set(int index, T element) {
        rangeCheck(index);

        T oldValue = getElement(index);
        array[index] = element;
        return oldValue;
    }

    @Override
    public void add(int index, T element) {
        rangeCheck(index);

        checkCapacity(1);
        System.arraycopy(array, index, array, index + 1,
                size - index);
        array[index] = element;
        size++;
    }

    @Override
    public T remove(int index) {
        rangeCheck(index);

        T oldValue = getElement(index);
        internalRemove(index);
        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; ++i)
                if (o.equals(array[i]))
                    return i;
        } else {
            for (int i = 0; i < size; ++i)
                if (null == array[i])
                    return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o != null) {
            for (int i = size - 1; i >= 0; --i)
                if (o.equals(array[i]))
                    return i;
        } else {
            for (int i = size - 1; i >= 0; --i)
                if (null == array[i])
                    return i;
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        rangeCheck(index);

        return new ListItr(index);
    }

    private class ListItr implements ListIterator<T> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        ListItr(int index) {
            super();
            cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = MyArrayList.this.array;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (T) elementData[lastRet = i];
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                MyArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @SuppressWarnings("unchecked")
        public T previous() {
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = MyArrayList.this.array;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (T) elementData[lastRet = i];
        }

        @Override
        public void set(T e) {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                MyArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(T e) {
            try {
                int i = cursor;
                MyArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    private void checkCapacity(int decrease) {
        int newSize;
        capacity -= decrease;
        if (capacity <= 0) {
            newSize = Math.max(size * 2, size + decrease);
            capacity = newSize - size;
            reCreate(newSize);
        }
    }

    @Override
    public Object clone() {
        try {
            MyArrayList<?> v = (MyArrayList<?>) super.clone();
            v.array = Arrays.copyOf(array, size);
            v.capacity = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (T e : this)
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        return hashCode;
    }

    private void reCreate(int newSize) {
        array = Arrays.copyOf(array, newSize);
    }

    private void internalRemove(int index) {
        int different = size - index - 1;
        if (different > 0)
            System.arraycopy(array, index + 1, array, index,
                    different);
        array[--size] = null; // clear to let GC do its work
        capacity++;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index > this.size)
            throw new IndexOutOfBoundsException("Index: " + index);
    }

    @SuppressWarnings("unchecked")
    private T getElement(int index) {
        return (T) array[index];
    }
}