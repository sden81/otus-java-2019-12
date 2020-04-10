package jdbc.queryGenerator;

public interface SqlGenerator<T> {
    public String getInsertQuery();

    public String getSelectQuery();

    public String getUpdateQuery();
}
