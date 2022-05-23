package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.io.Serializable;

public interface ErasureCodes extends Serializable, Comparable<ErasureCodes> {
    void encodeDataObject( DataObject dataObject, int n_blocks );
    DataObject decodeDataObject(DataObject object) throws ErasureCodesImpl.IncompleteBlockException;

    void addBlockAt( ErasureCodesImpl.ErasureBlock erasureBlock );

    ErasureCodesImpl.ErasureBlock[] getErasureBlocks();
    ErasureCodesImpl.ErasureBlock getBlockAt(int position );
}
