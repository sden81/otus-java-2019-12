package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.services.UserService;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/user/list"})
    public String userListView(Model model) {
        var usersList = userService.getAllUsers();
        usersList.ifPresent(persons -> model.addAttribute("usersList", persons));
        return "userList.html";
    }
}
