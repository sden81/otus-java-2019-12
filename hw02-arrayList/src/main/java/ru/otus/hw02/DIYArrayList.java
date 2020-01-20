package ru.otus.hw02;

import java.util.*;

public class DIYArrayList<T> implements List<T> {
    protected T[] internalArray;
    private final int DEFAULT_CAPACITY = 1;
    protected int listSize = 0;

    public DIYArrayList(int capacity) {
        capacity = capacity > 0 ? capacity : DEFAULT_CAPACITY;
        internalArray = (T[]) new Object[capacity];
    }

    public DIYArrayList() {
        internalArray = (T[]) new Object[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return listSize;
    }

    @Override
    public boolean isEmpty() {
        return listSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < listSize; i++) {
            if (internalArray[i].equals(o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (listSize == internalArray.length) {
            incInternalArray();
        }
        internalArray[listSize++] = t;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T el : c) {
            add(el);
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index > size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        T[] shiftedSubArray = Arrays.copyOfRange(internalArray, index, size());
        listSize = index;
        for (T el : c) {
            add(el);
        }

        for (T el2 : shiftedSubArray) {
            add(el2);
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        checkIndex(index);

        return internalArray[index];
    }

    @Override
    public T set(int index, T element) {
        checkIndex(index);
        T oldValue = internalArray[index];
        internalArray[index] = element;

        return oldValue;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort(internalArray, 0, size(), c);
    }


    /**
     *
     */
    protected void incInternalArray() {
        internalArray = Arrays.copyOf(internalArray, internalArray.length * 2);
    }

    /**
     * @param index
     */
    protected void checkIndex(int index) {
        if (index > listSize || index < 0) {
            throw new IndexOutOfBoundsException();
        }
    }
}
