package ru.otus.hw02;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.*;

public class DIYArrayListTest {

    @Test
    public void add() {
        DIYArrayList<Integer> myDIYArrayList = new DIYArrayList<>();
        myDIYArrayList.add(1);
        myDIYArrayList.add(2);
        myDIYArrayList.add(3);
        assertEquals(3, (int) myDIYArrayList.get(2));
    }

    @Test
    public void contains() {
        DIYArrayList<Integer> testDIYArrayList = getTestDIYArrayList();

        assertEquals(true, testDIYArrayList.contains(10));
        assertEquals(false, testDIYArrayList.contains(100));
    }

    @Test
    public void addAll() {
        DIYArrayList<Integer> testDIYArrayList = getTestDIYArrayList();
        ArrayList<Integer> addedList = new ArrayList<>();
        addedList.add(100);
        addedList.add(101);
        addedList.add(102);
        testDIYArrayList.addAll(addedList);

        assertEquals(100, (int) testDIYArrayList.get(25));
        assertEquals(102, (int) testDIYArrayList.get(27));

        testDIYArrayList.addAll(5, addedList);
        assertEquals(100, (int) testDIYArrayList.get(5));
        assertEquals(5, (int) testDIYArrayList.get(8));
    }

    @Test
    public void collectionsAddAll() {
        DIYArrayList<Integer> testDIYArrayList = getTestDIYArrayList();

        Collections.addAll(testDIYArrayList, 100);
        assertEquals(100, (int) testDIYArrayList.get(25));

        Collections.addAll(testDIYArrayList, 101, 102);
        assertEquals(102, (int) testDIYArrayList.get(27));
    }

    @Test
    public void collectionsCopy() {
        DIYArrayList<Integer> dstDIYArrayList = getTestDIYArrayList();
        DIYArrayList<Integer> srcDIYArrayList = getAnotherTestDIYArrayList();
        Collections.copy(dstDIYArrayList, srcDIYArrayList);

        assertEquals(1000, (int) dstDIYArrayList.get(0));
        assertEquals(24, (int) dstDIYArrayList.get(dstDIYArrayList.size()-1));
    }

    @Test
    public void collectionsSort(){
        DIYArrayList<Integer> testDIYArrayList = getTestDIYArrayList();
        testDIYArrayList.set(0,2);
        testDIYArrayList.set(1,3);
        testDIYArrayList.set(2,1);
        testDIYArrayList.set(8,100);

        Collections.sort(testDIYArrayList, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.equals(o2) ? 0 : (o1 > o2 ? 1 : -1);
            }
        });
        assertEquals(1, (int) testDIYArrayList.get(0));
        assertEquals(3, (int) testDIYArrayList.get(2));
        assertEquals(100, (int) testDIYArrayList.get(testDIYArrayList.size()-1));
    }

    /**
     * @return DIYArrayList<Integer>
     */
    protected DIYArrayList<Integer> getTestDIYArrayList() {
        DIYArrayList<Integer> testDIYArrayList = new DIYArrayList<>();
        for (int i = 0; i < 25; i++) {
            testDIYArrayList.add(i);
        }

        return testDIYArrayList;
    }

    /**
     * @return DIYArrayList<Integer>
     */
    protected DIYArrayList<Integer> getAnotherTestDIYArrayList() {
        DIYArrayList<Integer> testDIYArrayList = new DIYArrayList<>();
        for (int i = 0; i < 23; i++) {
            testDIYArrayList.add(1000 + i);
        }

        return testDIYArrayList;
    }

}