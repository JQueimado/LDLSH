package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import org.jetbrains.annotations.NotNull;

public class ReedSolomonErasureCodes implements ErasureCodes {

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
    public int compareTo(@NotNull ErasureCodes o) {
        return 0;
    }
}
