package ru.otus.services;

import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

  long saveUser(User user);

  Optional<User> getUser(long id);
  Optional<User> getUser(String login);
  Optional<User> getRandomUser();
  Optional<List<User>> getAllUsers();
}
