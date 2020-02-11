package ru.otus.hw05;

import ru.otus.hw05.annotations.Before;
import ru.otus.hw05.annotations.After;
import ru.otus.hw05.annotations.Test;

public class TestSuite {

    @Before
    public void beforeTest1() {
        System.out.println("Execute TestSuite.beforeTest1");
    }

    @Before
    public void beforeTest2() {
        System.out.println("Execute TestSuite.beforeTest2");
    }

    @Test
    public void successTest() {
        System.out.println("Execute TestSuite.testSuccess");
    }

    @Test
    public void errorTest() {
        System.out.println("Execute TestSuite.errorTest");
        throw new AssertionError("Error in test");
    }

    @After
    public void endTest1() {
        System.out.println("Execute TestSuite.endTest1");
    }

    @After
    public void endTest2() {
        System.out.println("Execute TestSuite.endTest2");
    }
}
