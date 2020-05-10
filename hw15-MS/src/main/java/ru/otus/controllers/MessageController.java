package ru.otus.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import ru.otus.model.Message;
import ru.otus.model.User;
import ru.otus.services.UserService;

import java.util.Base64;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final UserService userService;

    public MessageController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/message.getAllUsers")
    @SendTo("/topic/response")
    public Message getAllUsers(Message message) {
        logger.info("got message:{}", message);
        String jsonAllUsers = new Gson().toJson(userService.getAllUsers());
        return new Message(jsonAllUsers);
    }

    @MessageMapping("/message.addNewUser")
    @SendTo("/topic/response")
    public Message addNewUser(Message message) {
        logger.info("got message:{}", message);
        String originalBase64Message = message.getMessageStr();
        byte[] decodedBytes = Base64.getDecoder().decode(originalBase64Message);
        String decodedString = new String(decodedBytes);

        User user = new Gson().fromJson(decodedString, User.class);
        userService.saveUser(user);
        String jsonAllUsers = new Gson().toJson(userService.getAllUsers());
        return new Message(jsonAllUsers);
    }
}
