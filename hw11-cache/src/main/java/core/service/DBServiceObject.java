package core.service;

import java.util.Optional;

public interface DBServiceObject<T> {

  long saveObject(T object);

  Optional<T> getObject(long id);

  void updateObject(long id, T object);

  long insertOrUpdateObject(T object);
}
