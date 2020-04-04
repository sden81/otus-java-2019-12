package cachehw;

import java.util.Optional;

/**
 * @author sergey
 * created on 14.12.18.
 */
public interface HwCache<K, V> {

  void put(K key, V value);

  void remove(K key);

  Optional<V> get(K key);

  void addListener(HwListener<K, V> listener);

  void removeListener(HwListener<K, V> listener);

  void clear();

  long listenerCount();
}
