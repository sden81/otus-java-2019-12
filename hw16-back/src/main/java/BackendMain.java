import core.model.Address;
import core.model.Phone;
import core.model.User;
import core.service.DBServiceUser;
import core.service.DbServiceUserImpl;
import hibernate.HibernateUtils;
import hibernate.dao.UserDaoHibernate;
import hibernate.sessionmanager.SessionManagerHibernate;
import msHandlers.request.GetAllUsersDataRequestHandler;
import msHandlers.request.GetUserDataRequestHandler;
import msHandlers.request.SaveUserDataRequestHandler;
import ru.otus.MessageType;
import ru.otus.MsClient;
import ru.otus.MsClientSocketImpl;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class BackendMain {
  public static final String MESSAGE_SERVER_ADDRESS = "localhost";
  public static final int MESSAGE_SERVER_PORT = 5556;
  public static final String BACKEND_SERVICE_CLIENT_NAME = "backendService";

  private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";

  public static void main(String[] args) throws Exception {
    var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, Address.class, Phone.class);
    var sessionManager = new SessionManagerHibernate(sessionFactory);
    var userDaoHibernate = new UserDaoHibernate(sessionManager);
    var dbServiceUser = new DbServiceUserImpl(userDaoHibernate);

    try {
      BackendMain.createDefaultUsers(dbServiceUser);
//      var allUsers = dbServiceUser.getAllUsers();

      Socket socket = new Socket(MESSAGE_SERVER_ADDRESS, MESSAGE_SERVER_PORT);
      MsClient backendMsClient = new MsClientSocketImpl(BACKEND_SERVICE_CLIENT_NAME, socket);
      backendMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbServiceUser));
      backendMsClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersDataRequestHandler(dbServiceUser));
      backendMsClient.addHandler(MessageType.SAVE_USER, new SaveUserDataRequestHandler(dbServiceUser));

      while(socket.isConnected()){
        Thread.sleep(500);
      }

    } finally {
      sessionManager.close();
      sessionFactory.close();
    }
  }

  public static void createDefaultUsers(DBServiceUser userService){
    Address address1 = new Address("Address1");
    Set<Phone> phones1 = new HashSet<>();
    phones1.add(new Phone("123"));
    phones1.add(new Phone("456"));

    Address address2 = new Address("Address2");
    Set<Phone> phones2 = new HashSet<>();
    phones2.add(new Phone("123"));
    phones2.add(new Phone("456"));

    Address address3 = new Address("Address3");
    Set<Phone> phones3 = new HashSet<>();
    phones3.add(new Phone("123"));
    phones3.add(new Phone("456"));

    userService.saveUser(new User("Вася", "123", address1, phones1));
    userService.saveUser(new User("Петя", "123", address2, phones2));
    userService.saveUser(new User("Вова", "123", address3, phones3));
  }
}
