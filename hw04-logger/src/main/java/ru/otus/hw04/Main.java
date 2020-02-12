package ru.otus.hw04;

public class Main {
    public static void main(String[] args) {
        DI IoC = new DI();
        OriginalInterface original = IoC.createOriginal();
        OriginalInterface proxy = IoC.createProxyFromOriginal(original);
        proxy.hello(1);
    }
}