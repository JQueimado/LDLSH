package SystemLayer.Processes;

import SystemLayer.Data.DataObject;
import SystemLayer.Data.ObjectName;

public interface ObjectNameProcessor {
    public ObjectName getName(DataObject dataObject);
}
