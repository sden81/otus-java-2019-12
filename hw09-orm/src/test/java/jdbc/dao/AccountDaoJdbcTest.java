package jdbc.dao;

import core.dao.ModelDao;
import core.model.Account;
import core.service.DBServiceObject;
import core.service.DbServiceAccount;
import demo.DbServiceDemo;
import h2.DataSourceH2;
import jdbc.DbExecutor;
import jdbc.queryGenerator.SqlGeneratorImpl;
import jdbc.sessionmanager.SessionManagerJdbc;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountDaoJdbcTest {
    private DBServiceObject<Account> dbServiceAccount;

    @Before
    public void init() throws SQLException {
        DataSource dataSource = new DataSourceH2();
        DbServiceDemo demo = new DbServiceDemo(dataSource);

        SqlGeneratorImpl<Account> sqlGenerator = new SqlGeneratorImpl<>(Account.class);
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor<Account> dbExecutor = new DbExecutor<>();
        ModelDao accountDao = new AccountDaoJdbc(sessionManager, dbExecutor, sqlGenerator);
        dbServiceAccount = new DbServiceAccount(accountDao);
    }

    @Test
    public void testSave() {
        Long insertingId = dbServiceAccount.saveObject(new Account(1,"type",2));
        assertEquals(1, insertingId.intValue());
    }

    @Test
    public void testUpdate() {
        Long insertingId = dbServiceAccount.saveObject(new Account(1,"type",2));
        dbServiceAccount.updateObject(insertingId, new Account(1,"qwe",3));
        Optional<Account> account = dbServiceAccount.getObject(insertingId);
        assertTrue( account.isPresent());
    }


    @Test
    public void testInsertOrUpdate() {
        Long insertingId = dbServiceAccount.insertOrUpdateObject(new Account(0,"type",2));
        assertEquals(1, insertingId.intValue());

        Long result = dbServiceAccount.insertOrUpdateObject(new Account(insertingId,"qwe",3));
        assertTrue(result.equals(-1L));

        Optional<Account> account = dbServiceAccount.getObject(insertingId);
        assertEquals("qwe", account.get().getType());
    }
}