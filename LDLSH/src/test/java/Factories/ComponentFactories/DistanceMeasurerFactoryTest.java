package Factories.ComponentFactories;

import SystemLayer.Processes.DistanceMeasurerImpl.DistanceMeasurer;
import SystemLayer.Processes.DistanceMeasurerImpl.JaccardDistance;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DistanceMeasurerFactoryTest {

    @Test
    void getNewDistanceMeasurer_NONE() throws Exception {
        String type = "NONE";
        DistanceMeasurerFactory distanceMeasurerFactory = new DistanceMeasurerFactory();
        DistanceMeasurer distanceMeasurer = distanceMeasurerFactory.getNewDistanceMeasurer( type );
        assertNull( distanceMeasurer );
    }

    @Test
    void getNewDistanceMeasurer_JACCARD() throws Exception {
        String type = "JACCARD";
        DistanceMeasurerFactory distanceMeasurerFactory = new DistanceMeasurerFactory();
        DistanceMeasurer distanceMeasurer = distanceMeasurerFactory.getNewDistanceMeasurer( type );
        assertNotNull(distanceMeasurer);
        assertEquals( distanceMeasurer.getClass(), JaccardDistance.class);
    }
}