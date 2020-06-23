package ru.otus;


public interface RequestHandler {
    Message handle(Message msg);

    boolean isNeedResponse();
}
