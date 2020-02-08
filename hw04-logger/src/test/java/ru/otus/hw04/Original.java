package ru.otus.hw04;

public class Original implements OriginalInterface {

    public void justMethod() {
    }

    @Log
    public void hello(int i) {
        System.out.println("Hello");
    }
}
