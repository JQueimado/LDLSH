package SystemLayer.Components.PackerImpl;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public interface Packer {
    public Package[] pack(UniqueIdentifier objectName, LSHHash lsh, ErasureCodes codes);
}
