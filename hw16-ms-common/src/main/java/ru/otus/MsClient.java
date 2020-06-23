package ru.otus;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface MsClient {

    void addHandler(MessageType type, RequestHandler requestHandler);

    void sendMessage(Message msg) throws IOException;

    Optional<Message> receiveMessage();

    String getName();

    <T> Message produceMessage(String to, T data, MessageType msgType);
    <T> Message produceMessage(String to, T data, MessageType msgType, UUID sourceMessageId);
}
