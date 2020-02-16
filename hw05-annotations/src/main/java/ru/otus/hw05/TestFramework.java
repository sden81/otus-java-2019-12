package ru.otus.hw05;

import ru.otus.hw05.annotations.Before;
import ru.otus.hw05.annotations.After;
import ru.otus.hw05.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFramework {
    protected Class testClass;
    protected List<Method> beforeMethods = new ArrayList<>();
    protected List<Method> testMethods = new ArrayList<>();
    protected List<Method> endMethods = new ArrayList<>();

    public TestFramework(Class testClass) {
        this.testClass = testClass;
        fillMethodLists();
    }

    protected void fillMethodLists() {
        for (Method method : testClass.getMethods()) {
            if (method.getAnnotation(Before.class) != null) {
                beforeMethods.add(method);
                continue;
            }
            if (method.getAnnotation(Test.class) != null) {
                testMethods.add(method);
                continue;
            }
            if (method.getAnnotation(After.class) != null) {
                endMethods.add(method);
            }
        }
    }

    public void runTests() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (testMethods.size() < 1) {
            throw new RuntimeException("Empty tests");
        }

        int faultTests = 0;

        for (Method testMethod : testMethods) {
            Object testSuiteInstance = testClass.getDeclaredConstructor().newInstance();
            System.out.println("");
            System.out.println("-------------------------");
            System.out.printf("Run test %s", testMethod.getName());
            System.out.println("");

            if (!runBeforeAfter(beforeMethods, testSuiteInstance)){
                faultTests++;
                runBeforeAfter(endMethods, testSuiteInstance);
                continue;
            }

            //run test
            try {
                testMethod.invoke(testSuiteInstance);
            } catch (Exception e) {
                faultTests++;
                System.out.println("");
                System.out.printf("Exception %s in method %s with description %s%s",
                        e.getClass().toString(),
                        testMethod.getName(),
                        e.getMessage(),
                        System.lineSeparator()
                );
            }

            if (!runBeforeAfter(endMethods, testSuiteInstance)){
                faultTests++;
            }
        }
        System.out.println("");
        System.out.println("-------------------------");
        System.out.printf("%nSuccess tests: %d %nFault tests: %d %nTotal tests: %d",
                testMethods.size() - faultTests,
                faultTests,
                testMethods.size()
        );
    }

    protected boolean runBeforeAfter(List<Method> methodsList, Object instance) throws InvocationTargetException, IllegalAccessException {
        if (methodsList.size() < 1) {
            return true;
        }

        for (Method runMethod : methodsList) {
            try {
                runMethod.invoke(instance);
            } catch (Exception e) {
                System.out.printf("Exception %s in method %s with description",
                        e.getClass().toString(),
                        runMethod.getName(),
                        e.getMessage()
                );
                return false;
            }
        }

        return true;
    }

}
