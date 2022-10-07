package SystemLayer.Components.DistanceMeasurerImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public interface DistanceMeasurer {
    double getDistance(byte[] object_a, byte[] object_b);
    double getDistance(int[] object_a, int[] object_b);
}
