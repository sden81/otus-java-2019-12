package core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.dao.ModelDao;
import core.model.User;
import core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceUser implements DBServiceObject<User> {
  private static Logger logger = LoggerFactory.getLogger(DbServiceUser.class);

  private final ModelDao userDao;

  public DbServiceUser(ModelDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public long saveObject(User user) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        long userId = userDao.saveObject(user);
        sessionManager.commitSession();

        logger.info("created user: {}", userId);
        return userId;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public Optional<User> getObject(long id) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<User> userOptional = userDao.findById(id);

        logger.info("user: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }

  @Override
  public void updateObject(long id, User user) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        userDao.updateObject(id, user);
        sessionManager.commitSession();

        logger.info("update user: {}", id);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public long insertOrUpdateObject(User user) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        long userId = userDao.insertOrUpdateObject(user);
        sessionManager.commitSession();

        if (userId == user.getId()){
          logger.info("update user: {}", userId);
        } else{
          logger.info("insert user: {}", userId);
        }
        return userId;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

}
