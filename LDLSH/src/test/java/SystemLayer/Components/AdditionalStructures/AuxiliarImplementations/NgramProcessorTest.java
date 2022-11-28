package SystemLayer.Components.AdditionalStructures.AuxiliarImplementations;

import SystemLayer.Containers.DataContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NgramProcessorTest {

    private int ngramLevel = 3;
    private DataContainer simulatedState;

    @BeforeEach
    void before(){
        simulatedState = new DataContainer("");
        simulatedState.getConfigurator().setConfig("NGRAMS_LEVEL", Integer.toString(ngramLevel));
    }

    @Test
    void create_ngrams() throws Exception {

        byte[] data = new byte[]{0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09};
        Set<Integer> expected = new HashSet<>();
        expected.add(0x00010203);
        expected.add(0x00020304);
        expected.add(0x00030405);
        expected.add(0x00040506);
        expected.add(0x00050607);
        expected.add(0x00060708);
        expected.add(0x00070809);

        Set<Integer> result = NgramProcessor.create_ngrams(data, simulatedState);

        Assertions.assertEquals(expected, result);
    }
}