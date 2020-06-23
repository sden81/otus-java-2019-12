package ru.otus;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.helpers.Serializers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

public class MsClientSocketImpl implements MsClient {
    private static final Logger logger = LoggerFactory.getLogger(MsClientSocketImpl.class);

    public static final String CLIENT_TYPE_FRONT = "front";
    public static final String CLIENT_TYPE_BACK = "back";
    private final String name;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();

    private volatile Socket socket;
    private final ExecutorService inputOutputWorkers = Executors.newFixedThreadPool(2);
    private final BlockingQueue<Message> output = new ArrayBlockingQueue<>(100);
    private final BlockingQueue<Message> input = new ArrayBlockingQueue<>(100);


    public MsClientSocketImpl(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
        startWorkers();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        handlers.put(type.getValue(), requestHandler);
    }

    @Override
    public void sendMessage(Message msg) {
        try {
            output.put(msg);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Message> receiveMessage() {
        try {
            logger.info("Wait message");
            Message receivedMsg = input.take();
            logger.info("Receive message: {}", receivedMsg);

            return Optional.of(receivedMsg);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return Optional.empty();
    }


    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(name, to, null, msgType.getValue(), Serializers.serialize(data));
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType, UUID sourceMessageId) {
        return new Message(name, to, sourceMessageId, msgType.getValue(), Serializers.serialize(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientSocketImpl msClient = (MsClientSocketImpl) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    protected void startWorkers() {
        inputOutputWorkers.submit(() -> sendMessageThread(socket));
        inputOutputWorkers.submit(() -> receiveMessageThread(socket));
    }

    private void sendMessageThread(Socket socket) {
        logger.info("Start sendMessageThread");
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                Message msg = output.take();
                logger.info("Send message: {}", msg);
                String json = new Gson().toJson(msg);
                out.println(json);
            }
        } catch (Exception e) {
            logger.info("Send message error: {}", e.getMessage());
            inputOutputWorkers.shutdown();
        }
    }

    private void receiveMessageThread(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (!inputLine.isEmpty()) {
                    Message msg = new Gson().fromJson(inputLine, Message.class);
                    logger.info("receiveMessageThread Receiving message: {}", msg);
                    if (msg != null) {
                        if (handlers.containsKey(msg.getType())) {
                            RequestHandler handler = handlers.get(msg.getType());
                            Message handledMessage = handler.handle(msg);

                            if (handler.isNeedResponse()) {
                                sendMessage(handledMessage);
                            }
                        } else {
                            input.add(msg);

                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info("Receive message error: {}", e.getMessage());
            inputOutputWorkers.shutdown();
        }
    }
}
