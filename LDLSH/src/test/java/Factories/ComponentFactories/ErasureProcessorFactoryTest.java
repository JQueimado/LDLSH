package Factories.ComponentFactories;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErasureProcessorFactoryTest {

    @Test
    void setCurrentType_NONE() throws Exception {
        String type = "NONE";
        ErasureProcessorFactory.setCurrentType(type);
        assertEquals(ErasureProcessorFactory.current_type, ErasureProcessorFactory.types.NONE);
    }

    @Test
    void getNewInstance_NONE() {

    }
}