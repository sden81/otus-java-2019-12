package core.service;

import cachehw.HwCache;
import core.dao.ModelDao;
import core.model.User;
import core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DbServiceUser implements DBServiceObject<User> {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUser.class);

    private final ModelDao userDao;
    private HwCache<String, User> cache;

    public DbServiceUser(ModelDao userDao, HwCache<String, User> cache) {
        this.cache = cache;
        this.userDao = userDao;
    }

    @Override
    public long saveObject(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long userId = userDao.saveObject(user);
                sessionManager.commitSession();

                cache.put(String.valueOf(userId), user);

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
                Optional<User> cachedUser = cache.get(String.valueOf(id));
                if (cachedUser.isPresent()) {
                    return cachedUser;
                }

                Optional<User> userOptional = userDao.findById(id);
                userOptional.ifPresent(user -> cache.put(String.valueOf(id), user));

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

                cache.remove(String.valueOf(id));
                cache.put(String.valueOf(id), user);

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

                if (userId == user.getId()) {
                    logger.info("update user: {}", userId);
                } else {
                    logger.info("insert user: {}", userId);
                }

                cache.put(String.valueOf(userId), user);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

}
