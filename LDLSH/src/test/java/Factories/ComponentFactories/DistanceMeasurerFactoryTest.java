package Factories.ComponentFactories;

import SystemLayer.Processes.DistanceMeasurer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DistanceMeasurerFactoryTest {

    @Test
    void setCurrentType() throws Exception {
        String type;

        //NONE
        type = "NONE";
        DistanceMeasurerFactory.setCurrentType(type);
        assertEquals(DistanceMeasurerFactory.current_type, DistanceMeasurerFactory.types.NONE);

        //STANDARD
        type = "STANDARD";
        DistanceMeasurerFactory.setCurrentType(type);
        assertEquals(DistanceMeasurerFactory.current_type, DistanceMeasurerFactory.types.STANDARD);
    }

    @Test
    void getNewDistanceMeasurer() throws Exception {
        String type;

        //NONE
        type = "NONE";
        DistanceMeasurerFactory.setCurrentType(type);

        DistanceMeasurer distanceMeasurer = DistanceMeasurerFactory.getNewDistanceMeasurer();
        assertNull( distanceMeasurer );
    }
}