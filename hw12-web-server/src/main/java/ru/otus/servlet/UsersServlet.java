package ru.otus.servlet;

import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
//    private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";
    private static final String TEMPLATE_ATTR_ALL_USER = "allUsers";

    private final UserService userService;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, UserService userService) {
        this.templateProcessor = templateProcessor;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        userService.getAllUsers().ifPresent(users -> paramsMap.put(TEMPLATE_ATTR_ALL_USER, users));

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
