package ru.otus.hw01;

import com.google.common.base.CharMatcher;

public class HelloOtus {
    public static void main(String[] args) {
        String input = "    Hello world   ";
        System.out.println(HelloOtus.trim(input));
    }

    public static String trim (String inputString){
        return CharMatcher.whitespace().trimFrom(inputString);
    }
}
