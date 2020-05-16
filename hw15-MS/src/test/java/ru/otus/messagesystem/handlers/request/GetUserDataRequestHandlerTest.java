package ru.otus.messagesystem.handlers.request;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.helpers.Serializers;
import ru.otus.model.User;
import ru.otus.services.UserService;
import ru.otus.messagesystem.Message;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetUserDataRequestHandlerTest {
    private final User sampleUser = new User(1, "userName", "userLogin", "userPassword");

    @DisplayName("Обработчик получения данных по конкретному юзеру")
    @Test
    public void testUserDataRequestHandler(){
        var userService = mock(UserService.class);
        var getUserDataRequestHandler = new GetUserDataRequestHandler(userService);
        var message = mock(Message.class);

        when(userService.getUser(anyLong())).thenReturn(Optional.of(sampleUser));
        when(message.getPayload()).thenReturn(Serializers.serialize(1L));

        Message returnMessage = getUserDataRequestHandler.handle(message).get();
        String jsonUser = Serializers.deserialize(returnMessage.getPayload(), String.class);
        User returnedUser = new Gson().fromJson(jsonUser, User.class);
        assertTrue(returnedUser.equals(sampleUser));
    }
}