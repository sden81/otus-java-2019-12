package ru.otus.messagesystem;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.Message;
import ru.otus.MsClientSocketImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class MessageSystemImpl implements MessageSystem {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);
    private static final int MESSAGE_QUEUE_SIZE = 100_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;

    private final AtomicBoolean runFlag = new AtomicBoolean(true);
    private final Map<Integer, String> clientMap = new ConcurrentHashMap<>();
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

    private Runnable disposeCallback;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT,
            new ThreadFactory() {

                private final AtomicInteger threadNameSeq = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
                    return thread;
                }
            });

    //threads for connections
    private final Map<Integer, BlockingQueue<String>> threadsInput = Collections.synchronizedMap(new HashMap<>());
    private final Map<Integer, BlockingQueue<String>> threadsOutput = Collections.synchronizedMap(new HashMap<>());

    public MessageSystemImpl() {
        start();
    }

    public MessageSystemImpl(boolean startProcessing) {
        if (startProcessing) {
            start();
        }
    }

    @Override
    public void start() {
        msgProcessor.submit(this::processMessages);

        //создаем потоки на 2 подключения (сервер подключений для фронта и для бэка)
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> serverThreadHandler(MsClientSocketImpl.CLIENT_TYPE_FRONT, 5555));
        executor.submit(() -> serverThreadHandler(MsClientSocketImpl.CLIENT_TYPE_BACK, 5556));

        //создаем один воркер, который просматривает входящие очереди подключений
        // и пересылает сообщения в MS (обработав перед этим заголовки с источником и адресатом)
        Executors.newSingleThreadExecutor().submit(this::messageReceiver);
    }

    @Override
    public int currentQueueSize() {
        return messageQueue.size();
    }

    @Override
    public void addClient(String clientType, int port) {
        logger.info("new client by name: {}", clientType + port);
        clientMap.put(port, clientType);
    }

    /**
     * @param port
     */
    @Override
    public void removeClient(int port) {
        logger.info("remove client by id: {}", port);
        clientMap.computeIfPresent(port, (p, t) -> clientMap.remove(p));
    }

    public boolean newMessage(Message msg) {
        if (runFlag.get()) {
            return messageQueue.offer(msg);
        } else {
            logger.warn("MS is being shutting down... rejected:{}", msg);
            return false;
        }
    }

    @Override
    public Optional<Integer> getRandomBackedClientPort() {
        var backendClients = clientMap.entrySet().stream()
                .filter(entry -> MsClientSocketImpl.CLIENT_TYPE_BACK.equals(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (backendClients.isEmpty()) {
            return Optional.empty();
        }

        Object[] keysArray = backendClients.keySet().toArray();
        Integer port = (Integer) keysArray[new Random().nextInt(keysArray.length)];

        return Optional.of(port);
    }

    /**
     * @throws InterruptedException
     */
    @Override
    public void dispose() throws InterruptedException {
        logger.info("now in the messageQueue {} messages", currentQueueSize());
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        msgHandler.awaitTermination(60, TimeUnit.SECONDS);
    }

    /**
     * @param callback
     * @throws InterruptedException
     */
    @Override
    public void dispose(Runnable callback) throws InterruptedException {
        disposeCallback = callback;
        dispose();
    }

    private void processMessages() {
        logger.info("msgProcessor started, {}", currentQueueSize());
        while (runFlag.get() || !messageQueue.isEmpty()) {
            try {
                Message msg = messageQueue.take();
                if (msg == Message.VOID_MESSAGE) {
                    logger.info("received the stop message");
                } else {
                    Integer port = Integer.parseInt(msg.getTo());
                    if (port > 0 && clientMap.containsKey(port)) {
                        threadsOutput.get(port).add((new Gson()).toJson(msg));
                    }
                }
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        if (disposeCallback != null) {
            msgHandler.submit(disposeCallback);
        }
        msgHandler.submit(this::messageHandlerShutdown);
        logger.info("msgProcessor finished");
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
        logger.info("msgHandler has been shut down");
    }

    /**
     * @throws InterruptedException
     */
    private void insertStopMessage() throws InterruptedException {
        boolean result = messageQueue.offer(Message.VOID_MESSAGE);
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(Message.VOID_MESSAGE);
        }
    }

    /**
     * @param clientType
     * @param port
     */
    private void serverThreadHandler(String clientType, int port) {
        logger.info("Start server on port {}", port);
        //на одно подключение используем 2 воркера, которые заполняют input/output очереди
        var executor = Executors.newFixedThreadPool(4);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("waiting for {} client connection", clientType);
                Socket clientSocket = serverSocket.accept();
                addClient(clientType, clientSocket.getPort());
                executor.submit(() -> sendMessageThread(clientSocket));
                executor.submit(() -> receiveMessageThread(clientSocket));
            }
        } catch (Exception ex) {
            logger.error("ServerSocket error: {}", ex.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    /**
     * @param socket
     */
    private void sendMessageThread(Socket socket) {
        logger.info("Start sendMessageThread");
        threadsOutput.put(socket.getPort(), new ArrayBlockingQueue<>(100));
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                String msg = threadsOutput.get(socket.getPort()).take();
                out.println(msg);
            }
        } catch (Exception e) {
            logger.info("Send message error: {}", e.getMessage());
        } finally {
            threadsOutput.remove(socket.getPort());
            removeClient(socket.getPort());
        }
    }

    /**
     * @param socket
     */
    private void receiveMessageThread(Socket socket) {
        logger.info("Start receiveMessageThread");
        threadsInput.put(socket.getPort(), new ArrayBlockingQueue<>(100));
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine = null;
            while (!"stop".equals(inputLine)) {
                inputLine = in.readLine();
                if (!inputLine.isEmpty()) {
                    threadsInput.get(socket.getPort()).add(inputLine);
                }
            }
        } catch (Exception e) {
            logger.info("Receive message error: {}", e.getMessage());
        } finally {
            threadsInput.remove(socket.getPort());
            removeClient(socket.getPort());
        }
    }

    private void messageReceiver() {
        logger.info("Start messageReceiver");
        while (true) {
            threadsInput.forEach((port, queue) -> {
                if (queue.size() > 0) {
                    String jsonMessage = queue.remove();
                    if (!jsonMessage.isEmpty()) {
                        Optional.of(handleReceiveMessage(jsonMessage, port)).ifPresent(
                                msg -> {
                                    newMessage(msg.get());
                                    logger.info("Got message: {}", msg.get());
                                }
                        );
                    }
                }
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    /**
     * Обрабатываем маршрутную информацию и собираем сообщение
     * @param jsonMessage
     * @param sourcePort
     * @return
     */
    private Optional<Message> handleReceiveMessage(String jsonMessage, Integer sourcePort) {
        Message originalMessage = new Gson().fromJson(jsonMessage, Message.class);
        String to;
        if (originalMessage.getTo().equals(MsClientSocketImpl.CLIENT_TYPE_BACK)) {
            Optional<Integer> randomBackendPort = getRandomBackedClientPort();
            if (randomBackendPort.isEmpty()) {
                logger.info("Backed client not found. Rejected message: {}", originalMessage);
                return Optional.empty();
            }
            to = randomBackendPort.get().toString();
        } else {
            to = originalMessage.getTo();
        }

        return Optional.of(new Message(
                sourcePort.toString(),
                to,
                originalMessage.getSourceMessageId().orElse(UUID.randomUUID()),
                originalMessage.getType(),
                originalMessage.getPayload()
        ));
    }

    public static void main(String[] args) {
        MessageSystem ms = new MessageSystemImpl();
    }

}
