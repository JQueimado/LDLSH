package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.io.Serializable;

public interface LSHHash extends Serializable, Comparable<LSHHash> {
    void setObject( DataObject object, int n_blocks );
    byte[] getSignature();

    LSHHashBlock[] getBlocks();
    LSHHashBlock getBlockAt( int position );

    record LSHHashBlock( byte[] lshBlock ){
        /**/
    }
}
