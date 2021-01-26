package org.luvx.pattern.behavioral.iterator;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class MyArrayList<E> implements MyCollection<E> {
    transient Object[] data;
    private   int      size;

    public MyArrayList() {
        this.data = new Object[16];
    }

    @Override
    public boolean add(E o) {
        data[size++] = o;
        return true;
    }

    @Override
    public E remove(int index) {
        E oldValue = (E) data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(data, index + 1, data, index, numMoved);
        }
        data[--size] = null;
        return oldValue;
    }

    private void fastRemove(int i) {
        int numMoved = size - i - 1;
        if (numMoved > 0) {
            System.arraycopy(data, i + 1, data, i, numMoved);
        }
        data[--size] = null;
    }

    @Override
    public MyIterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements MyIterator<E> {
        int cursor;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public E next() {
            int i = cursor;
            if (i >= size) {
                throw new NoSuchElementException();
            }
            Object[] data = MyArrayList.this.data;
            if (i >= data.length) {
                throw new ConcurrentModificationException();
            }
            cursor = i + 1;
            return (E) data[lastRet = i];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            MyArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }
    }
}