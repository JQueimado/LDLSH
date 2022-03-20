package SystemLayer.Processes.LSHHashFactory;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.LSHHashImpl.LSHHash;

public interface LSHHashProcessor {
    LSHHash getLSH(DataObject dataObject, DataContainer dataContainer);
}
