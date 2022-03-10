package SystemLayer.ProcessInterfacesses;

import SystemLayer.Data.DataObject;

import javax.xml.crypto.Data;

public interface DistanceMeasurer {
    int getDistance(DataObject object_a, DataObject object_b);
}
