package SystemLayer.Components.DistanceMeasurerImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public interface DistanceMeasurer {
    double getDistance(byte[] object_a, byte[] object_b);
}
