package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.io.Serializable;

public interface LSHHash extends Serializable {
    void setObject( DataObject object );
    byte[] getValues();
    byte[][] getBlocks(int n_blocks);
}
