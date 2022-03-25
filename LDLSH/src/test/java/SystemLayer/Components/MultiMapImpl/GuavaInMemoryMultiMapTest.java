package SystemLayer.Components.MultiMapImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuavaInMemoryMultiMapTest {

    private MultiMap multiMap;

    @BeforeEach
    void before(){
        multiMap = new GuavaInMemoryMultiMap(1,1);

    }

    @Test
    void insert() {
    }

    @Test
    void complete() {
    }

    @Test
    void query() {
    }
}