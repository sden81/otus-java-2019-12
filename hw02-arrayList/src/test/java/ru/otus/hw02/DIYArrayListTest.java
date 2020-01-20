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

    /**
     * @return DIYArrayList<Integer>
     */
    public DIYArrayList<Integer> getTestDIYArrayList() {
        DIYArrayList<Integer> testDIYArrayList = new DIYArrayList<>();
        for (int i = 0; i < 25; i++) {
            testDIYArrayList.add(i);
        }

        return testDIYArrayList;
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
        DIYArrayList<Integer> srcDIYArrayList = new DIYArrayList<>();
        srcDIYArrayList.add(100);
        srcDIYArrayList.add(101);
        Collections.copy(dstDIYArrayList, srcDIYArrayList);

        assertEquals(100, (int) dstDIYArrayList.get(0));
        assertEquals(3, (int) dstDIYArrayList.get(3));
    }

    @Test
    public void collectionsSort(){
        DIYArrayList<Integer> testDIYArrayList = getTestDIYArrayList();
        testDIYArrayList.set(0,2);
        testDIYArrayList.set(1,3);
        testDIYArrayList.set(2,1);

        Collections.sort(testDIYArrayList, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.equals(o2) ? 0 : (o1 > o2 ? 1 : -1);
            }
        });
        assertEquals(1, (int) testDIYArrayList.get(0));
        assertEquals(3, (int) testDIYArrayList.get(2));
    }

}