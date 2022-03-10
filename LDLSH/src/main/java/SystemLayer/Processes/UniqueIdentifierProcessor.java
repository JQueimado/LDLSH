package SystemLayer.Processes;

import SystemLayer.Data.DataObject;
import SystemLayer.Data.UniqueIdentifier;

public interface UniqueIdentifierProcessor {
    public UniqueIdentifier getName(DataObject dataObject);
}
