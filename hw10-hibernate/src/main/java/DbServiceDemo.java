import core.model.Address;
import core.model.Phone;
import core.model.User;
import core.service.DbServiceUserImpl;
import hibernate.HibernateUtils;
import hibernate.dao.UserDaoHibernate;
import hibernate.sessionmanager.SessionManagerHibernate;

import java.util.HashSet;
import java.util.Set;

public class DbServiceDemo {
  private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";

  public static void main(String[] args) throws Exception {
    var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, Address.class, Phone.class);
    var sessionManager = new SessionManagerHibernate(sessionFactory);
    var userDaoHibernate = new UserDaoHibernate(sessionManager);
    var dbServiceUser = new DbServiceUserImpl(userDaoHibernate);

    User user = DbServiceDemo.buildDefaultUser();
    dbServiceUser.saveUser(user);
    var loadedUser = dbServiceUser.getUser(user.getId());

    sessionManager.close();
    sessionFactory.close();
  }

  public static User buildDefaultUser() {
    Address address = new Address("Address1");

    Set<Phone> phones = new HashSet<>();
    phones.add(new Phone("123"));
    phones.add(new Phone("456"));

    return new User("Вася", address, phones);
  }
}
