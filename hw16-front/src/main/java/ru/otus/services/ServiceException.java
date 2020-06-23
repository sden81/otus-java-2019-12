package ru.otus.services;

import ru.otus.CommonException;

public class ServiceException extends CommonException {
  public ServiceException(Exception e) {
    super(e);
  }
}
