package ru.otus.controllers;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.services.UserService;

@RestController
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-api")
    public String getUser(@RequestParam(name = "id") Long id) {
        var user = userService.getUser(id);
        return new Gson().toJson(user.get());
    }
}
