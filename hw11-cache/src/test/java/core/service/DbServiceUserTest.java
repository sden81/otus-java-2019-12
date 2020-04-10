package core.service;

import cachehw.HWCacheDemo;
import cachehw.HwCache;
import cachehw.HwListener;
import cachehw.MyCache;
import core.dao.ModelDao;
import core.model.User;
import demo.DbServiceDemo;
import h2.DataSourceH2;
import jdbc.DbExecutor;
import jdbc.dao.UserDaoJdbc;
import jdbc.queryGenerator.SqlGeneratorImpl;
import jdbc.sessionmanager.SessionManagerJdbc;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DbServiceUserTest {
    private static DBServiceObject<User> userService;
    private static HwCache<String, User> cache;
    private static HwListener<String, User> listener;
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeAll
    public static void init() throws SQLException {
        DataSource dataSource = new DataSourceH2();
        DbServiceDemo demo = new DbServiceDemo(dataSource);
        cache = new MyCache<>();
        listener = new HwListener<String, User>() {
            @Override
            public void notify(String key, User value, String action) {
                System.out.printf("key:%s, value:%s, action:%s", key.toString(), value.toString(), action);
            }
        };
        cache.addListener(listener);

        SqlGeneratorImpl<User> sqlGenerator = new SqlGeneratorImpl<>(User.class);
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor<User> dbExecutor = new DbExecutor<>();
        ModelDao userDao = new UserDaoJdbc(sessionManager, dbExecutor, sqlGenerator);
        userService = new DbServiceUser(userDao, cache);
    }

    @AfterAll
    public static void end() {
        cache.removeListener(listener);
    }

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        cache.clear();
    }

    @DisplayName("проверяем сохранение объекта в кеша")
    @Test
    void saveObject() {
        User user = createUser2();
        userService.saveObject(user);
        assertEquals(user, cache.get(String.valueOf(user.getId())).get());
    }

    @DisplayName("проверяем получение объекта из кеша")
    @Test
    void getObject() {
        User user = createUser1();
        userService.saveObject(user);
        outContent.reset();
        assertEquals(user, userService.getObject(user.getId()).get());
        assertEquals("key:1, value:User{id=1, name='Jerry'}, action:get", outContent.toString());
    }

    private User createUser1() {
        return new User(1, "Jerry");
    }

    private User createUser2() {
        return new User(2, "Tom");
    }
}