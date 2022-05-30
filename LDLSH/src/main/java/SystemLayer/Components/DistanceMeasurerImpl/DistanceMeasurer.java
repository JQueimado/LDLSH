package SystemLayer.Components.DistanceMeasurerImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public interface DistanceMeasurer {
    double getDistance(DataObject object_a, DataObject object_b);
}
