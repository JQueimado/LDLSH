package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.io.Serializable;
import java.util.Arrays;

public interface ErasureCodes extends Serializable, Comparable<ErasureCodes> {
    void encodeDataObject( DataObject dataObject, int n_blocks );
    DataObject decodeDataObject(DataObject object) throws IncompleteBlockException;

    void addBlockAt( ErasureBlock erasureBlock );

    ErasureBlock[] getErasureBlocks();
    ErasureBlock getBlockAt( int blocks );

    record ErasureBlock( byte[] block_data, int position ) implements Comparable<ErasureBlock> {
        @Override
        public int compareTo(ErasureBlock o) {
            if ( position != o.position )
                return -1;
            return Arrays.compare(block_data, o.block_data);
        }

        @Override
        public boolean equals(Object obj) {
            if( obj.getClass() != ErasureBlock.class )
                return false;
            return this.compareTo( (ErasureBlock) obj ) == 0 ;
        }
    }

    class IncompleteBlockException extends Exception{
        public IncompleteBlockException( String message ){
            super(message);
        }
        public IncompleteBlockException(){
            super();
        }
    }
}
