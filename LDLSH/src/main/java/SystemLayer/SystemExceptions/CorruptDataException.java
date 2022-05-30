package SystemLayer.SystemExceptions;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class CorruptDataException extends Exception {

    public void handler( CorruptDataException e ){

    }

    public ErasureCodes erasureCodes;
    public UniqueIdentifier uid;

    public CorruptDataException(String message, ErasureCodes erasureCodes, UniqueIdentifier uniqueIdentifier) {
        super(message);
        this.erasureCodes = erasureCodes;
        this.uid = uniqueIdentifier;
    }
}
