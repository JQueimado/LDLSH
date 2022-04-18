package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public class SimplePartitionErasureCodes implements ErasureCodes {

    private ErasureBlock[] erasureBlocks;

    public SimplePartitionErasureCodes( int total_blocks ){
        erasureBlocks = new ErasureBlock[total_blocks];
    }

    @Override
    public void encodeDataObject(DataObject dataObject, int n_blocks) {

    }

    @Override
    public DataObject decodeDataObject() {
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
