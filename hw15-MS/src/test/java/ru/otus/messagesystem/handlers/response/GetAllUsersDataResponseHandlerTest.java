package ru.otus.messagesystem.handlers.response;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.helpers.Serializers;
import ru.otus.WebConfig;
import ru.otus.model.User;
import ru.otus.services.front.FrontendService;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;

import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAllUsersDataResponseHandlerTest {
    private List<User> allUsers = new ArrayList<>(Arrays.asList(
            new User(1, "userName1", "userLogin1", "userPassword1"),
            new User(2, "userName2", "userLogin2", "userPassword2")
    ));

    @DisplayName("Обработчик получения данных по всем юзерам")
    @Test
    public void TestGetAllUsersDataResponseHandler(){
        var frontendService = mock(FrontendService.class);
        var getAllUsersDataResponseHandler = new GetAllUsersDataResponseHandler(frontendService);
        var consumer = new Consumer<>() {
            @Override
            public void accept(Object receivedJson) {
                assertEquals(new Gson().toJson(allUsers), receivedJson);
            }
        };
        when(frontendService.takeConsumer(any(),any())).thenReturn(Optional.of(consumer));
        getAllUsersDataResponseHandler.handle(getSampleMessage());
    }

    private Message getSampleMessage() {
        String dataJson = new Gson().toJson(allUsers);
        return new Message(
                WebConfig.DATABASE_SERVICE_CLIENT_NAME,
                WebConfig.FRONTEND_SERVICE_CLIENT_NAME,
                UUID.randomUUID(),
                MessageType.ALL_USERS_DATA.toString(),
                Serializers.serialize(dataJson)
        );
    }
}