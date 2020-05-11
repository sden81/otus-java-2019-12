package ru.otus.messagesystem.handlers.response;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.WebConfig;
import ru.otus.helpers.Serializers;
import ru.otus.model.User;
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

class GetUserDataResponseHandlerTest {
    private final User sampleUser = new User(1, "userName", "userLogin", "userPassword");

    @DisplayName("Обработчик получения данных по конкретному юзеру")
    @Test
    public void getUserDataResponseHandlerTest() {
        var frontendService = mock(FrontendService.class);
        var getUserDataResponseHandler = new GetUserDataResponseHandler(frontendService);
        var consumer = new Consumer<>() {
            @Override
            public void accept(Object receivedJson) {
                assertEquals(new Gson().toJson(sampleUser), receivedJson);
            }
        };
        when(frontendService.takeConsumer(any(),any())).thenReturn(Optional.of(consumer));
        getUserDataResponseHandler.handle(getSampleMessage());
    }

    private Message getSampleMessage() {
        String dataJson = new Gson().toJson(sampleUser);
        return new Message(
                WebConfig.DATABASE_SERVICE_CLIENT_NAME,
                WebConfig.FRONTEND_SERVICE_CLIENT_NAME,
                UUID.randomUUID(),
                MessageType.USER_DATA.toString(),
                Serializers.serialize(dataJson)
        );
    }
}