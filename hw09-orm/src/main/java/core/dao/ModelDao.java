package core.dao;

import core.sessionmanager.SessionManager;

import java.util.Optional;

public interface ModelDao<T> {
  Optional<T> findById(long id);

  long saveObject(T object);

  void updateObject(long id, T object);

  long insertOrUpdateObject(T object);

  SessionManager getSessionManager();
}
