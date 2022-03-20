package SystemLayer.Processes.PackerImpl;

import SystemLayer.Data.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIdentifier;

public class StandardPacker implements Packer{
    @Override
    public Package[] pack(UniqueIdentifier objectName, LSHHash lsh, ErasureCodes codes) {
        return new Package[0];
    }
}
