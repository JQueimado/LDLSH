package SystemLayer.Data.UniqueIndentifierImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.io.Serializable;

public interface UniqueIdentifier extends Serializable, Comparable<UniqueIdentifier> {
    void setObject( byte[] object );
    byte[] getUID();
    void setBytes( byte[] bytes );
}
