package demo;

import core.dao.ModelDao;
import core.model.User;
import core.service.DBServiceObject;
import core.service.DbServiceUser;
import h2.DataSourceH2;
import jdbc.DbExecutor;
import jdbc.dao.UserDaoJdbc;
import jdbc.queryGenerator.SqlGeneratorImpl;
import jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public DbServiceDemo(DataSource dataSource) throws SQLException {
        createTable(dataSource);
    }

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSourceH2();
        DbServiceDemo demo = new DbServiceDemo(dataSource);

        SqlGeneratorImpl<User> sqlGenerator = new SqlGeneratorImpl<>(User.class);
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor<User> dbExecutor = new DbExecutor<>();

        ModelDao userDao = new UserDaoJdbc(sessionManager, dbExecutor, sqlGenerator);

        DBServiceObject dbServiceUser = new DbServiceUser(userDao);
        long id = dbServiceUser.saveObject(new User(0, "dbServiceUser"));
        Optional<User> user = dbServiceUser.getObject(id);

        System.out.println(user);
        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );

        dbServiceUser.updateObject(id, new User(0, "updatedDbServiceUser"));
        Optional<User> updatedUser = dbServiceUser.getObject(id);
        System.out.println(updatedUser);

        long anotherUpdatedUserId = dbServiceUser.insertOrUpdateObject(new User(id, "anotherUpdatedDbServiceUser"));
        Optional<User> anotherUpdatedUser = dbServiceUser.getObject(anotherUpdatedUserId);
        System.out.println(anotherUpdatedUser);

        long newUserId = dbServiceUser.insertOrUpdateObject(new User(100, "newUser"));
        Optional<User> newUser = dbServiceUser.getObject(newUserId);
        System.out.println(newUser);
    }

    private void createTable(DataSource dataSource) throws SQLException {
        var initDbQueries = new ArrayList<String>();
        initDbQueries.add("create table user(id long auto_increment, name varchar(50))");
        initDbQueries.add("CREATE TABLE account(no IDENTITY auto_increment NOT NULL, type varchar(255), rest int)");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     String.join(";", initDbQueries))) {
            pst.executeUpdate();
        }
        System.out.println("tables created");
    }
}
