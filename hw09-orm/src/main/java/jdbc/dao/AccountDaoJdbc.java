package jdbc.dao;

import core.dao.AbstractModelDao;
import core.dao.ModelDao;
import core.dao.DaoException;
import core.model.Account;
import core.sessionmanager.SessionManager;
import jdbc.DbExecutor;
import jdbc.queryGenerator.SqlGenerator;
import jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoJdbc extends AbstractModelDao implements ModelDao<Account> {
    private final DbExecutor<Account> dbExecutor;
    private final SqlGenerator<Account> sqlGenerator;

    public AccountDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<Account> dbExecutor, SqlGenerator<Account> sqlGenerator)
    {
        super(sessionManager);
        this.dbExecutor = dbExecutor;
        this.sqlGenerator = sqlGenerator;
    }

    @Override
    public Optional<Account> findById(long id) {
        try {
            String selectQuery = sqlGenerator.getSelectQuery();
            return dbExecutor.selectRecord(getConnection(), selectQuery, id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return new Account(resultSet.getLong("no"), resultSet.getString("type"), resultSet.getInt("rest"));
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
    public long saveObject(Account account) {
        String insertQuery = sqlGenerator.getInsertQuery();
        List<String> params = new ArrayList<>();
        params.add(String.valueOf(account.getRest()));
        params.add(account.getType());
        try {
            return dbExecutor.insertRecord(getConnection(), insertQuery, params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public void updateObject(long id, Account account) {
        String updateQuery = sqlGenerator.getUpdateQuery();
        List<String> params = new ArrayList<>();
        params.add(String.valueOf(account.getRest()));
        params.add(account.getType());
        try {
            dbExecutor.updateRecord(getConnection(), updateQuery, id, params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public long insertOrUpdateObject(Account account) {
        var checkAccount = findById(account.getNo());
        if (checkAccount.isEmpty()) {
            return saveObject(account);
        } else {
            updateObject(account.getNo(), account);
            return -1;
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
