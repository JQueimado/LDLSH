package SystemLayer.ProcessInterfacesses;

import SystemLayer.Data.DataObject;
import SystemLayer.Data.LocalitySensitiveHashing;

public interface LSHProcessor {
    public LocalitySensitiveHashing getLSH(DataObject dataObject);
}
