package Factories.ComponentFactories;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DistanceMeasurerFactoryTest {

    @Test
    void setCurrentType_NONE() throws Exception {
        String type = "NONE";
        DistanceMeasurerFactory.setCurrentType(type);
        assertEquals(DistanceMeasurerFactory.current_type, DistanceMeasurerFactory.types.NONE);
    }

    @Test
    void getNewDistanceMeasurer() {
    }
}