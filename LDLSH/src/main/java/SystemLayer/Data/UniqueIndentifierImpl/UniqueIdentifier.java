package SystemLayer.Data.UniqueIndentifierImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public interface UniqueIdentifier {
    void setObject(DataObject dataObject);
    byte[] getUID();
}
