package SystemLayer.Data.DataUnits;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.io.Serializable;

public record ModelMultimapValue(LSHHash lshHash,
                                 UniqueIdentifier uniqueIdentifier,
                                 ErasureCodesImpl.ErasureBlock erasureCode
) implements MultiMapValue {

    public ModelMultimapValue(LSHHash lshHash, UniqueIdentifier uniqueIdentifier, ErasureCodesImpl.ErasureBlock erasureCode) {
        this.lshHash = lshHash;
        this.uniqueIdentifier = uniqueIdentifier;
        this.erasureCode = erasureCode;
    }
}
