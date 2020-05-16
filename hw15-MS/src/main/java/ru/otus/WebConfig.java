package ru.otus;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import ru.otus.helpers.HibernateUtils;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.messagesystem.handlers.request.GetAllUsersDataRequestHandler;
import ru.otus.messagesystem.handlers.request.GetUserDataRequestHandler;
import ru.otus.messagesystem.handlers.request.SaveUserDataRequestHandler;
import ru.otus.messagesystem.handlers.response.GetAllUsersDataResponseHandler;
import ru.otus.messagesystem.handlers.response.GetUserDataResponseHandler;
import ru.otus.messagesystem.handlers.response.SaveUserDataResponseHandler;
import ru.otus.model.User;
import ru.otus.services.UserService;
import ru.otus.services.front.FrontendService;
import ru.otus.services.front.FrontendServiceImpl;

@Configuration
@ComponentScan
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";
    private final ApplicationContext applicationContext;
    public static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    public static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, User.class);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/WEB-INF/static/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/")
                .resourceChain(false);
        registry.setOrder(1);
    }

    @Bean(name = "backendMsClient")
    public MsClient backendMsClient(MessageSystemImpl messageSystem, UserService userService) {
        MsClient backendMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
        backendMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(userService));
        backendMsClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersDataRequestHandler(userService));
        backendMsClient.addHandler(MessageType.SAVE_USER, new SaveUserDataRequestHandler(userService));
        messageSystem.addClient(backendMsClient);

        return backendMsClient;
    }

    @Bean(name = "frontendMsClient")
    public MsClient frontendMsClient(MessageSystemImpl messageSystem, FrontendService frontendService) {
        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
        frontendMsClient.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.SAVE_USER, new SaveUserDataResponseHandler(frontendService));
        messageSystem.addClient(frontendMsClient);

        return frontendMsClient;
    }
}
