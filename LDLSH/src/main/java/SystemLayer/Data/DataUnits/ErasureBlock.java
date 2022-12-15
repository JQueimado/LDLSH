package SystemLayer.Data.DataUnits;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;

import java.io.Serializable;
import java.util.Arrays;

//Subclasses
public interface ErasureBlock extends Comparable<ErasureBlock>, Serializable {
    void setPosition( int position );
    void setBlock( byte[] block );
    int getPosition();
    byte[] getBlock();
}
