package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public interface ErasureCodes {
    void encodeDataObject( DataObject dataObject );
    DataObject decodeDataObject();

    byte[] getErasureCodes();
    byte[][] getErasureBlocks();
}
