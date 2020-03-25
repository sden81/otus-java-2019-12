package jdbc.queryGenerator;

import core.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SqlGeneratorImplTest {
    private User user;
    private SqlGeneratorImpl<User> sqlGenerator;

    @Before
    public void init() {
        user = new User(1, "Peter");
        sqlGenerator = new SqlGeneratorImpl<>(user.getClass());
    }

    @Test
    public void getIdFieldName() {
        String idFieldName = sqlGenerator.getIdFieldName();
        assertEquals("id", idFieldName);
    }

    @Test
    public void getFieldNames() {
        var types = sqlGenerator.getFieldTypes();
        assertEquals(2, types.size());
    }

    @Test
    public void getInsertQuery() {
        String insertQuery = sqlGenerator.getInsertQuery();
        assertEquals("INSERT INTO user(name) VALUES (?)", insertQuery);
    }

    @Test
    public void getSelectQuery() {
        String selectQuery = sqlGenerator.getSelectQuery();
        assertEquals("SELECT name,id FROM user WHERE id = ?", selectQuery);
    }

    @Test
    public void getUpdateQuery() {
        String updateQuery = sqlGenerator.getUpdateQuery();
        assertEquals("UPDATE user SET name = ? WHERE id = ?", updateQuery);
    }

    @Test
    public void getQuestionMarksFromFieldsString(){
        String questionMarks = sqlGenerator.getQuestionMarksFromFieldsString("a,b,c,d,f");
        assertEquals("?,?,?,?,?", questionMarks);
    }
}