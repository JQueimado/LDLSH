package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public interface ErasureCodes {
    void encodeDataObject( DataObject dataObject, int n_blocks );
    DataObject decodeDataObject();

    void addBlockAt( ErasureBlock erasureBlock );

    ErasureBlock[] getErasureBlocks();
    ErasureBlock getBlockAt( int blocks );

    record ErasureBlock( byte[] block_data ){
        /**/
    }
}
