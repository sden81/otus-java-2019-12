package ru.otus.controllers;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.Message;
import ru.otus.services.UserService;
import ru.otus.services.front.FrontendService;

@RestController
public class UserRestController {
    private final UserService userService;
    private final FrontendService frontendService;
    private String result;

    public UserRestController(UserService userService, FrontendService frontendService) {
        this.userService = userService;
        this.frontendService = frontendService;
    }

    @GetMapping("/user-api")
    public String getUser(@RequestParam(name = "id") Long id) {
        frontendService.getAllUsers(data -> {
            result = data.toString();
        });

        return result;
    }
}
