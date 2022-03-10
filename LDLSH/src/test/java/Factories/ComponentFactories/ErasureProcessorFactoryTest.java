package Factories.ComponentFactories;

import SystemLayer.Processes.ErasureProcessorImpl.ErasureProcessor;
import SystemLayer.Processes.ErasureProcessorImpl.ReedSolomonErasureCodesProcessor;
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
    void getNewInstance_None() throws Exception {
        String type = "NONE";
        ErasureProcessorFactory.setCurrentType(type);
        ErasureProcessor erasureCodes = ErasureProcessorFactory.getNewInstance();
        assertNull(erasureCodes);
    }

    @Test
    void getNewInstance_ReedSolomon() throws Exception {
        String type = "REED_SOLOMON";
        ErasureProcessorFactory.setCurrentType(type);
        ErasureProcessor erasureCodesProcessor = ErasureProcessorFactory.getNewInstance();
        assert erasureCodesProcessor != null;
        assertEquals(erasureCodesProcessor.getClass(), ReedSolomonErasureCodesProcessor.class);
    }
}