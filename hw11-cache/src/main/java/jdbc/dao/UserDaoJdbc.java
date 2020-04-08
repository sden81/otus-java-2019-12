package jdbc.dao;


import core.dao.AbstractModelDao;
import core.dao.DaoException;
import core.dao.ModelDao;
import core.model.User;
import core.sessionmanager.SessionManager;
import jdbc.DbExecutor;
import jdbc.queryGenerator.SqlGenerator;
import jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

public class UserDaoJdbc extends AbstractModelDao implements ModelDao<User> {
//    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<User> dbExecutor;
    private final SqlGenerator<User> sqlGenerator;

    public UserDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<User> dbExecutor, SqlGenerator<User> sqlGenerator) {
        super(sessionManager);
        this.dbExecutor = dbExecutor;
        this.sqlGenerator = sqlGenerator;
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            String selectQuery = sqlGenerator.getSelectQuery();
            return dbExecutor.selectRecord(getConnection(), selectQuery, id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return new User(resultSet.getLong("id"), resultSet.getString("name"));
                    }
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
                return null;
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long saveObject(User user) {
        String insertQuery = sqlGenerator.getInsertQuery();
        try {
            return dbExecutor.insertRecord(getConnection(), insertQuery, Collections.singletonList(user.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public void updateObject(long id, User user) {
        String updateQuery = sqlGenerator.getUpdateQuery();
        try {
            dbExecutor.updateRecord(getConnection(), updateQuery, id, Collections.singletonList(user.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public long insertOrUpdateObject(User user) {
        var checkUser = findById(user.getId());
        if (checkUser.isEmpty()) {
            return saveObject(user);
        } else {
            updateObject(user.getId(), user);
            return -1;
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
