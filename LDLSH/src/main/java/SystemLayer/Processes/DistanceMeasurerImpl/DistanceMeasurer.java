package SystemLayer.Processes.DistanceMeasurerImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public interface DistanceMeasurer {
    int getDistance(DataObject object_a, DataObject object_b);
}
