package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

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
        System.out.println("See tests");
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
