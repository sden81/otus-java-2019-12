package ru.otus.messagesystem.handlers.request;

import com.google.gson.Gson;
import ru.otus.helpers.Serializers;
import ru.otus.model.User;
import ru.otus.services.UserService;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;

public class SaveUserDataRequestHandler implements RequestHandler {
    private final UserService userService;

    public SaveUserDataRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        String userJson = Serializers.deserialize(msg.getPayload(), String.class);
        User savingUSer = new Gson().fromJson(userJson, User.class);

        long result = userService.saveUser(savingUSer);

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.SAVE_USER.getValue(), Serializers.serialize(result)));
    }
}
