package common;

import core.model.Address;
import core.model.Phone;
import core.model.User;
import core.service.DbServiceUserImpl;
import hibernate.HibernateUtils;
import hibernate.dao.UserDaoHibernate;
import hibernate.sessionmanager.SessionManagerHibernate;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;

abstract public class HibernateAbstractTest {
    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";

    protected SessionFactory sessionFactory;
    protected SessionManagerHibernate sessionManager;
    protected UserDaoHibernate userDaoHibernate;
    protected DbServiceUserImpl dbServiceUser;

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, Address.class, Phone.class);
        sessionManager = new SessionManagerHibernate(sessionFactory);
        userDaoHibernate = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDaoHibernate);
    }

    @AfterEach
    void tearDown() {
        sessionManager.close();
        sessionFactory.close();
    }


    protected User buildDefaultUser() {
        Address address = new Address("Address1");

        Set<Phone> phones = new HashSet<>();
        phones.add(new Phone("123"));
        phones.add(new Phone("456"));

        return new User("Вася", address, phones);
    }
}
