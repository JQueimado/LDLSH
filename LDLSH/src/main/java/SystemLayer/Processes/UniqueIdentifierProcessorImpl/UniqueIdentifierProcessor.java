package SystemLayer.Processes.UniqueIdentifierProcessorImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIdentifier;

public interface UniqueIdentifierProcessor {
    public UniqueIdentifier getName(DataObject dataObject);
}
