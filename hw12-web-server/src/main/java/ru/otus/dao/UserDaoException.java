package ru.otus.dao;

import ru.otus.CommonException;

public class UserDaoException extends CommonException {
    public UserDaoException(String message) {
        super(message);
    }

    public UserDaoException(Exception ex) {
        super(ex);
    }
}
