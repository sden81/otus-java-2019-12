package ru.otus;

public class CommonException extends RuntimeException{
    public CommonException(String message) {
        super(message);
    }

    public CommonException(Exception ex) {
        super(ex);
    }
}
