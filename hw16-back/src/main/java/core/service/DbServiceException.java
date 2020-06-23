package core.service;

import core.Hw10CommonException;

public class DbServiceException extends Hw10CommonException {
  public DbServiceException(Exception e) {
    super(e);
  }
}
