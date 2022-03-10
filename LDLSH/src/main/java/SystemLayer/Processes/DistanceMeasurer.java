package SystemLayer.Processes;

import SystemLayer.Data.DataObject;

public interface DistanceMeasurer {
    int getDistance(DataObject object_a, DataObject object_b);
}
