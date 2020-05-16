package ru.otus.messagesystem.handlers.response;

import ru.otus.helpers.Serializers;
import ru.otus.services.front.FrontendService;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;
import java.util.UUID;

public class GetAllUsersDataResponseHandler implements RequestHandler {
    private final FrontendService frontendService;

    public GetAllUsersDataResponseHandler(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        String usersData = Serializers.deserialize(msg.getPayload(), String.class);
        UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
        frontendService.takeConsumer(sourceMessageId, String.class).ifPresent(consumer -> consumer.accept(usersData));

        return Optional.empty();
    }
}
