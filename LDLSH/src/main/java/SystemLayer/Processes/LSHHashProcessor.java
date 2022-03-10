package SystemLayer.Processes;

import SystemLayer.Data.DataObject;
import SystemLayer.Data.LSHHash;

public interface LSHHashProcessor {
    public LSHHash getLSH(DataObject dataObject);
}
