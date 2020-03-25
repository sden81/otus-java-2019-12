package core.dao;

import jdbc.dao.UserDaoJdbc;
import jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

abstract public class AbstractModelDao {
    protected Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);
    protected final SessionManagerJdbc sessionManager;

    public AbstractModelDao(SessionManagerJdbc sessionManager) {
        this.sessionManager = sessionManager;
    }

    protected Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
