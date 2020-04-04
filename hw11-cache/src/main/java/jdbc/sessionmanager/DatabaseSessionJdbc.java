package jdbc.sessionmanager;

import core.sessionmanager.DatabaseSession;

import java.sql.Connection;

public class DatabaseSessionJdbc implements DatabaseSession {
  private final Connection connection;

  DatabaseSessionJdbc(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return connection;
  }
}
