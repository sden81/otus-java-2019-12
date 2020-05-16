package ru.otus.messagesystem.handlers.request;

import com.google.gson.Gson;
import ru.otus.helpers.Serializers;
import ru.otus.services.UserService;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;

public class GetAllUsersDataRequestHandler implements RequestHandler {
    private final UserService userService;

    public GetAllUsersDataRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        String data = new Gson().toJson(userService.getAllUsers().get());

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.ALL_USERS_DATA.getValue(), Serializers.serialize(data)));
    }
}
