package ru.otus.hw05;

public class Main {
    public static void main(String... args) {
        Class testSuiteClass = TestSuite.class;

        TestFramework testFramework = new TestFramework(testSuiteClass);
        try {
            testFramework.runTests();
        } catch (Exception e) {
            System.out.printf("Exit with exception: %s", e.getMessage());
        }
    }
}
