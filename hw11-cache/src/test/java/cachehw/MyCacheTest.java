package cachehw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyCacheTest {
    private HwCache<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new MyCache<>();
    }

    @AfterEach
    void tearDown() {
        cache.clear();
    }

    @Test
    void remove() {
        cache.put("1", "23");
        assertThat(cache.get("1").get()).isEqualTo("23");
        cache.remove("1");
        assertThat(cache.get("1")).isNull();
    }

    @Test
    void get() {
        cache.put("1", "23");
        assertThat(cache.get("1").get()).isEqualTo("23");
    }


    @Test
    void addListener() {
        var listener = getListener();
        cache.addListener(listener);
        assertThat(cache.listenerCount()).isEqualTo(1);
        listener = null;
        System.gc();
        assertThat(cache.listenerCount()).isEqualTo(0);
    }

    @Test
    void removeListener() {
        var listener = getListener();
        cache.addListener(listener);
        assertThat(cache.listenerCount()).isEqualTo(1);
        cache.removeListener(listener);
        assertThat(cache.listenerCount()).isEqualTo(0);
    }

    private HwListener<String, String> getListener() {
        return new HwListener<String, String>() {
            @Override
            public void notify(String key, String value, String action) {
                System.out.println("some action");
            }
        };
    };
}