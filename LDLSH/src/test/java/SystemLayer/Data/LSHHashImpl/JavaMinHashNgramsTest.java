package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.DataContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JavaMinHashNgramsTest {

    @BeforeEach
    void beforeEach() throws Exception {
        DataContainer simulatedState = new DataContainer("");
        simulatedState.getConfigurator().setConfig("LSH_HASH", "JAVA_MINHASH_NGRAMS");
        simulatedState.getConfigurator().setConfig("NGRAMS_LEVEL", "3");
        new JavaMinHashNgrams( simulatedState ); //Setup
    }

    @Test
    void setObject() {
    }

    @Test
    void setupMinHash() {
    }

    @Test
    void create_ngrams() {
        byte[] test_data = new byte[]{1,2,3,4,5,6,7,8,9};

        byte[][] expected_result = new byte[][]{
                new byte[]{1,2,3},
                new byte[]{2,3,4},
                new byte[]{3,4,5},
                new byte[]{4,5,6},
                new byte[]{5,6,7},
                new byte[]{6,7,8},
                new byte[]{7,8,9}
        };

        byte[][] result_data = JavaMinHashNgrams.create_ngrams( test_data );

        for( int i=0; i < expected_result.length; i++ )
            assertArrayEquals(expected_result[i], result_data[i]);
    }
}