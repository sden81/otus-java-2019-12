package ru.otus.messagesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.*;
import ru.otus.helpers.Serializers;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageSystemImplTest {
    MessageSystem testedInstance = new MessageSystemImpl();

    @BeforeEach
    void setUp() {
        testedInstance = new MessageSystemImpl();
    }

    @Test
    void getRandomBackedClintPort() {
        Optional<Integer> empty = testedInstance.getRandomBackedClientPort();
        assertTrue(empty.isEmpty());

        testedInstance.addClient(MsClientSocketImpl.CLIENT_TYPE_BACK, 1);
        testedInstance.addClient(MsClientSocketImpl.CLIENT_TYPE_BACK, 2);

        Optional<Integer> backendPort = testedInstance.getRandomBackedClientPort();
        assertTrue(backendPort.get().equals(1) || backendPort.get().equals(2));
    }

    @Test
    void integrationTest() {
        MsClient backendClient = createBackendClient();
        MsClient backendClient2 = createBackendClient();
        MsClient frontendClient1 = createFrontendClient();
        MsClient frontendClient2 = createFrontendClient();

        Message getAllUsersMessage1 = frontendClient1.produceMessage(
                "back",
                new ArrayList<>(),
                MessageType.ALL_USERS_DATA,
                UUID.randomUUID()
        );
        Message getAllUsersMessage2 = frontendClient1.produceMessage(
                "back",
                new ArrayList<>(),
                MessageType.ALL_USERS_DATA,
                UUID.randomUUID()
        );
        try {
            frontendClient1.sendMessage(getAllUsersMessage1);
            frontendClient2.sendMessage(getAllUsersMessage2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Optional<Message> receivedMessage1 = frontendClient1.receiveMessage();
        assertTrue(receivedMessage1.isPresent());
        assertEquals(getAllUsersMessage1.getSourceMessageId(), receivedMessage1.get().getSourceMessageId());

        Optional<Message> receivedMessage2 = frontendClient2.receiveMessage();
        assertTrue(receivedMessage2.isPresent());
        assertEquals(getAllUsersMessage2.getSourceMessageId(), receivedMessage2.get().getSourceMessageId());
    }

    protected MsClient createFrontendClient() {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 5555);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new MsClientSocketImpl("frontClient", socket);
    }

    protected MsClient createBackendClient() {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 5556);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MsClient backendClient = new MsClientSocketImpl("backendClient", socket);
        backendClient.addHandler(MessageType.ALL_USERS_DATA, new RequestHandler() {
            @Override
            public Message handle(Message msg) {
                return new Message(msg.getTo(), msg.getFrom(), msg.getSourceMessageId().orElse(UUID.randomUUID()), MessageType.ALL_USERS_DATA.getValue(), Serializers.serialize(msg.getPayload()));
            }

            @Override
            public boolean isNeedResponse() {
                return true;
            }
        });

        return backendClient;
    }
}