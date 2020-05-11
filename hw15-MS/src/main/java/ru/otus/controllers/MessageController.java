package ru.otus.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.model.Message;
import ru.otus.model.User;
import ru.otus.services.front.FrontendService;

import java.util.Base64;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final SimpMessagingTemplate template;
    private final FrontendService frontendService;

    public MessageController(SimpMessagingTemplate template, FrontendService frontendService) {
        this.template = template;
        this.frontendService = frontendService;
    }
    
    @MessageMapping("/message.getAllUsers")
    public void getAllUsers(Message message) {
        logger.info("got message:{}", message);
        frontendService.getAllUsers(data -> {
            this.template.convertAndSend("/topic/getAllUsersResponse", new Message(data.toString()));
        });
    }

    @MessageMapping("/message.addNewUser")
    public void addNewUser(Message message) {
        logger.info("got message:{}", message);
        String originalBase64Message = message.getMessageStr();
        byte[] decodedBytes = Base64.getDecoder().decode(originalBase64Message);
        String decodedString = new String(decodedBytes);

        User user = new Gson().fromJson(decodedString, User.class);
        frontendService.saveUser(user, addedUserId -> {
            this.template.convertAndSend("/topic/getLastAddedUserIdResponse", new Message(addedUserId.toString()));
        });
    }
}
