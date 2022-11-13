package SystemLayer.Components.DistanceMeasurerImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.Set;

public interface DistanceMeasurer {
    double getDistance(int[] object_a, int[] object_b);
}
