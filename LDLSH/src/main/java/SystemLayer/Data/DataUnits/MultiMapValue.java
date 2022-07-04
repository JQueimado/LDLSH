package SystemLayer.Data.DataUnits;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.io.Serializable;

//Multimap value Class
public record MultiMapValue (
        LSHHash lshHash,
        UniqueIdentifier uniqueIdentifier,
        ErasureCodesImpl.ErasureBlock erasureCode
) implements Serializable {

    public MultiMapValue(LSHHash lshHash, UniqueIdentifier uniqueIdentifier, ErasureCodesImpl.ErasureBlock erasureCode) {
        this.lshHash = lshHash;
        this.uniqueIdentifier = uniqueIdentifier;
        this.erasureCode = erasureCode;
    }
}
