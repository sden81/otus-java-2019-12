package ru.otus.dao;

import ru.otus.model.User;
import ru.otus.services.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);
    Optional<User> findRandomUser();
    Optional<User> findByLogin(String login);
    Optional<List<User>> findAllUsers();
    long saveUser(User user);
    SessionManager getSessionManager();
}