package ru.otus.messagesystem;

import ru.otus.Message;

import java.util.Optional;

public interface MessageSystem {

//    void addClient(MsClient msClient);
    void addClient(String clientType, int port);

//    void removeClient(String clientId);

//    void removeClient(String clientType, int port);

    void removeClient(int port);

    boolean newMessage(Message msg);

    Optional<Integer> getRandomBackedClientPort();

//    boolean putMessage(String jsonMessage, int portSource);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();

    int currentQueueSize();
}

