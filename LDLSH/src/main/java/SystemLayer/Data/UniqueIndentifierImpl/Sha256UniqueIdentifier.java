package SystemLayer.Data.UniqueIndentifierImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public class Sha256UniqueIdentifier implements UniqueIdentifier{
    @Override
    public void setObject(DataObject dataObject) {

    }

    @Override
    public byte[] getUID() {
        return new byte[0];
    }
}
