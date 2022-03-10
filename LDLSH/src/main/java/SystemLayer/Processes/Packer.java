package SystemLayer.Processes;

import SystemLayer.Data.ErasureCodes;
import SystemLayer.Data.LSHHash;
import SystemLayer.Data.UniqueIdentifier;

public interface Packer {
    public Package[] pack(UniqueIdentifier objectName, LSHHash lsh, ErasureCodes codes);
}
