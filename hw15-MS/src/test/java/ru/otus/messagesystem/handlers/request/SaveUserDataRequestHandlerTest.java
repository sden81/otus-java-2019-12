package ru.otus.messagesystem.handlers.request;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.helpers.Serializers;
import ru.otus.model.User;
import ru.otus.services.UserService;
import ru.otus.messagesystem.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SaveUserDataRequestHandlerTest {
    private final User sampleUser = new User(1, "userName", "userLogin", "userPassword");

    @DisplayName("Обработчик сохранения юзера")
    @Test
    void TestSaveUserDataRequestHandler() {
        var userService = mock(UserService.class);
        var saveUserDataRequestHandler = new SaveUserDataRequestHandler(userService);
        var message = mock(Message.class);
        var userJson = new Gson().toJson(sampleUser);

        when(userService.saveUser(any())).thenReturn(1L);
        when(message.getPayload()).thenReturn(Serializers.serialize(userJson));

        Message returnMessage = saveUserDataRequestHandler.handle(message).get();
        var returnMessageBody = Serializers.deserialize(returnMessage.getPayload(), Long.class);
        assertEquals(1L, returnMessageBody);
    }
}