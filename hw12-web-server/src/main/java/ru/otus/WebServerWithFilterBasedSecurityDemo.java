package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.dao.UserDaoHibernate;
import ru.otus.helpers.HibernateUtils;
import ru.otus.model.User;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.*;
import ru.otus.sessionmanager.SessionManagerHibernate;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    Пользователь: admin/admin

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, User.class);
        var sessionManager = new SessionManagerHibernate(sessionFactory);
        var userDao = new UserDaoHibernate(sessionManager);
        var userService = new UserServiceImpl(userDao);

        userService.createDefaultUsers();

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userService);

        var usersWebServer = new UsersWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT,
                userService,
                authService,
                gson,
                templateProcessor
        );

        usersWebServer.start();
        usersWebServer.join();
    }
}
