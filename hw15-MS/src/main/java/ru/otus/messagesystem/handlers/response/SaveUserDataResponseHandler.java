package ru.otus.messagesystem.handlers.response;

import ru.otus.helpers.Serializers;
import ru.otus.services.front.FrontendService;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;
import java.util.UUID;

public class SaveUserDataResponseHandler implements RequestHandler {
    private final FrontendService frontendService;

    public SaveUserDataResponseHandler(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        Long userId = Serializers.deserialize(msg.getPayload(), Long.class);
        UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
        frontendService.takeConsumer(sourceMessageId, String.class).ifPresent(consumer -> consumer.accept(userId.toString()));

        return Optional.empty();
    }
}
