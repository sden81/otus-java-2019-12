package ru.otus.sessionmanager;


import ru.otus.CommonException;

public class SessionManagerException extends CommonException {
  public SessionManagerException(String msg) {
    super(msg);
  }

  public SessionManagerException(Exception ex) {
    super(ex);
  }
}
