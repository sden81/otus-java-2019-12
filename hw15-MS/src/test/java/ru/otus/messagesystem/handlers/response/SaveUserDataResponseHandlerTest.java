package ru.otus.messagesystem.handlers.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.WebConfig;
import ru.otus.helpers.Serializers;
import ru.otus.services.front.FrontendService;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SaveUserDataResponseHandlerTest {
    @DisplayName("Обработчик сохранения юзера")
    @Test
    void TestSaveUserDataResponseHandler() {
        var frontendService = mock(FrontendService.class);
        var saveUserDataResponseHandler = new SaveUserDataResponseHandler(frontendService);
        var consumer = new Consumer<>() {
            @Override
            public void accept(Object receivedJson) {
                assertEquals("1", receivedJson);
            }
        };
        when(frontendService.takeConsumer(any(),any())).thenReturn(Optional.of(consumer));
        saveUserDataResponseHandler.handle(getSampleMessage());
    }

    private Message getSampleMessage() {
        return new Message(
                WebConfig.DATABASE_SERVICE_CLIENT_NAME,
                WebConfig.FRONTEND_SERVICE_CLIENT_NAME,
                UUID.randomUUID(),
                MessageType.ALL_USERS_DATA.toString(),
                Serializers.serialize(1L)
        );
    }
}