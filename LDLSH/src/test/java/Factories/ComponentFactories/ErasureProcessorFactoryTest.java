package Factories.ComponentFactories;

import SystemLayer.Processes.ErasureProcessorImpl.ErasureProcessor;
import SystemLayer.Processes.ErasureProcessorImpl.ReedSolomonErasureCodesProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErasureProcessorFactoryTest {

    @Test
    void setCurrentType_NONE() throws Exception {
        String type = "NONE";
        ErasureProcessorFactory erasureProcessorFactory = new ErasureProcessorFactory();
        ErasureProcessor erasureProcessor = erasureProcessorFactory.getNewProcessor(type);
        assertNull(erasureProcessor);
    }

    @Test
    void getNewInstance_ReedSolomon() throws Exception {
        String type = "REED_SOLOMON";
        ErasureProcessorFactory erasureProcessorFactory = new ErasureProcessorFactory();
        ErasureProcessor erasureProcessor = erasureProcessorFactory.getNewProcessor(type);
        assertNotNull(erasureProcessor);
        assertEquals(erasureProcessor.getClass(), ReedSolomonErasureCodesProcessor.class);
    }
}