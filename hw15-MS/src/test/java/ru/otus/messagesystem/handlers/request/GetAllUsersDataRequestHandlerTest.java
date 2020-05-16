package ru.otus.messagesystem.handlers.request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.helpers.Serializers;
import ru.otus.model.User;
import ru.otus.services.UserService;
import ru.otus.messagesystem.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAllUsersDataRequestHandlerTest {
    private List<User> allUsers = new ArrayList<>(Arrays.asList(
            new User(1, "userName1", "userLogin1", "userPassword1"),
            new User(2, "userName2", "userLogin2", "userPassword2")
    ));

    @DisplayName("Обработчик получения данных по всем юзерам")
    @Test
    void getAllUsersData() {
        var userService = mock(UserService.class);
        var getAllUsersDataRequestHandler = new GetAllUsersDataRequestHandler(userService);
        var message = mock(Message.class);

        when(userService.getAllUsers()).thenReturn(Optional.of(allUsers));

        Message returnMessage = getAllUsersDataRequestHandler.handle(message).get();
        String jsonUsers = Serializers.deserialize(returnMessage.getPayload(), String.class);
        List<User> returnedUsers = new Gson().fromJson(jsonUsers, new TypeToken<List<User>>(){}.getType());
        assertEquals(allUsers.get(1), returnedUsers.get(1));
    }
}