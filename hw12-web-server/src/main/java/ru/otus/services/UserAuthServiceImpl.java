package ru.otus.services;

public class UserAuthServiceImpl implements UserAuthService {
    private final UserService userService;

    public UserAuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean authenticate(String login, String password) {
        var loggedUser = userService.getUser(login);
        if (loggedUser.isEmpty()) {
            return false;
        }

        return loggedUser.get().getPassword().equals(password);
    }
}
