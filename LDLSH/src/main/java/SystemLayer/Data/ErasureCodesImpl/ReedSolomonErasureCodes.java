package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public class ReedSolomonErasureCodes implements ErasureCodes {

    @Override
    public void encodeDataObject(DataObject dataObject, int n_blocks) {

    }

    @Override
    public DataObject decodeDataObject(DataObject object) {
        return null;
    }

    @Override
    public void addBlockAt(ErasureBlock erasureBlock) {

    }

    @Override
    public ErasureBlock[] getErasureBlocks() {
        return new ErasureBlock[0];
    }

    @Override
    public ErasureBlock getBlockAt(int blocks) {
        return null;
    }

    @Override
    public int compareTo(ErasureCodes o) {
        return 0;
    }
}
