package SystemLayer.Processes.PackerImpl;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class StandardPacker implements Packer{
    @Override
    public Package[] pack(UniqueIdentifier objectName, LSHHash lsh, ErasureCodes codes) {
        return new Package[0];
    }
}
