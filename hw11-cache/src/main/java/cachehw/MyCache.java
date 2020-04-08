package cachehw;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    private Map<K, V> storage = new WeakHashMap<>();
    private List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
    }

    @Override
    public Optional<V> get(K key) {
        try {
            var item = Optional.of(storage.get(key));
            if (item.isPresent()) {
                sendNotification(key, item.get(), "get");
            }
            return item;
        } catch (Exception $e) {
            return null;
        }
    }

    public void clear() {
        storage.clear();
        listeners.clear();
    }

    @Override
    public long listenerCount() {
        return listeners.stream().map(WeakReference::get).filter(Objects::nonNull).count();
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        var removedListener = listeners.stream().filter(reference -> Objects.equals(reference.get(), listener)).findAny();
        removedListener.ifPresent(hwListenerWeakReference -> listeners.remove(hwListenerWeakReference));
    }

    protected void sendNotification(K key, V value, String action) {
        listeners.stream().map(WeakReference::get).filter(Objects::nonNull).forEach((item) -> {
            item.notify(key,value, action);
        });
    }
}
