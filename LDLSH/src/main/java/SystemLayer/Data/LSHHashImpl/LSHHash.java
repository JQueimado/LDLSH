package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Set;

public interface LSHHash extends Serializable {
    void setObject( DataObject object );
    byte[] getValues();
    byte[][] getBlocks(int n_blocks);
}
