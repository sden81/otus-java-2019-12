package ru.otus.hw05;

import ru.otus.hw05.annotations.Before;
import ru.otus.hw05.annotations.End;
import ru.otus.hw05.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestFramework {
    protected Class testClass;
    protected ArrayList<Method> beforeMethods = new ArrayList<>();
    protected ArrayList<Method> testMethods = new ArrayList<>();
    protected ArrayList<Method> endMethods = new ArrayList<>();

    public TestFramework(Class testClass) {
        this.testClass = testClass;
        fillMethodLists();
    }

    protected void fillMethodLists() {
        for (Method method : testClass.getMethods()) {
            if (method.getAnnotation(Before.class) instanceof Before) {
                beforeMethods.add(method);
                continue;
            }
            if (method.getAnnotation(Test.class) instanceof Test) {
                testMethods.add(method);
                continue;
            }
            if (method.getAnnotation(End.class) instanceof End) {
                endMethods.add(method);
            }
        }
    }

    public void runTests() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (testMethods.size() < 1) {
            throw new RuntimeException("Empty tests");
        }

        int successTests = 0;
        int faultTests = 0;
        int errorTests = 0;


        Object testSuiteInstance = testClass.getDeclaredConstructor().newInstance();
        for (Method testMethod : testMethods) {
            System.out.println("-------------------------");
            System.out.printf("Run test %s", testMethod.getName());
            System.out.println("");

            runBeforeEnd(beforeMethods, testSuiteInstance);

            //run test
            try {
                Object testResult = testMethod.invoke(testSuiteInstance);
                if (testResult instanceof Boolean) {
                    if ((boolean) testResult) {
                        successTests++;
                    } else {
                        faultTests++;
                    }
                }
            } catch (Exception e) {
                errorTests++;
                System.out.printf("Exception %s in method %s with description %s%s",
                        e.getClass().toString(),
                        testMethod.getName(),
                        e.getMessage(),
                        System.lineSeparator()
                );
            }

            runBeforeEnd(endMethods, testSuiteInstance);
        }
        System.out.printf("Success tests: %d Fault tests: %d Error tests: %d",
                successTests,
                faultTests,
                errorTests
        );
    }

    protected void runBeforeEnd(ArrayList<Method> methodsList, Object instance) throws InvocationTargetException, IllegalAccessException {
        if (methodsList.size() < 1) {
            return;
        }

        for (Method runMethod : methodsList) {
            try {
                runMethod.invoke(instance);
            } catch (RuntimeException e) {
                System.out.printf("Exception %s in method %s with description",
                        e.getClass().toString(),
                        runMethod.getName(),
                        e.getMessage()
                );
            }
        }
    }

}
