package ru.otus;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private AtomicBoolean isNullThreadRun = new AtomicBoolean(true);
    private AtomicBoolean isCountedUp = new AtomicBoolean(true);
    private volatile long sharedCounter = 1;

    public static void main(String[] args) throws InterruptedException {
        Main counter = new Main();
        counter.go();
    }

    private void run() {
        String threadName = Thread.currentThread().getName();

        while (true) {
            if (threadName.toString().equals("Thread-0") && isNullThreadRun.get()) {
                System.out.printf("%s say: %d %s", threadName, sharedCounter, System.lineSeparator());
                isNullThreadRun.set(false);
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (threadName.toString().equals("Thread-1") && !isNullThreadRun.get()) {
                System.out.printf("%s say: %d %s", threadName, sharedCounter, System.lineSeparator());
                if (sharedCounter == 1) {
                    isCountedUp.set(true);
                }
                if (sharedCounter > 9) {
                    isCountedUp.set(false);
                }
                if (isCountedUp.get()) {
                    sharedCounter++;
                } else {
                    sharedCounter--;
                } ;
                isNullThreadRun.set(true);
            }
        }
    }

    private void go() {
        Thread thread0 = new Thread(this::run);
        Thread thread1 = new Thread(this::run);

        thread0.start();
        thread1.start();
    }
}
