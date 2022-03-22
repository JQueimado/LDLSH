package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public class ReedSolomonErasureCodes implements ErasureCodes {
    @Override
    public void encodeDataObject(DataObject dataObject) {

    }

    @Override
    public DataObject decodeDataObject() {
        return null;
    }

    @Override
    public byte[] getErasureCodes() {
        return new byte[0];
    }

    @Override
    public byte[][] getErasureBlocks() {
        return new byte[0][];
    }
}
