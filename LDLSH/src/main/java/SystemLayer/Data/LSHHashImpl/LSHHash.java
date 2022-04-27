package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import java.io.Serializable;
import java.util.Arrays;

public interface LSHHash extends Serializable, Comparable<LSHHash> {
    void setObject( DataObject object, int n_blocks );
    byte[] getSignature();

    LSHHashBlock[] getBlocks();
    LSHHashBlock getBlockAt( int position );

    record LSHHashBlock( byte[] lshBlock ) implements Comparable<LSHHashBlock>{
        @Override
        public int compareTo(LSHHashBlock o) {
            return Arrays.compare(lshBlock, o.lshBlock);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != LSHHashBlock.class)
                return false;
            LSHHashBlock conv = (LSHHashBlock) obj;
            return Arrays.equals( this.lshBlock, conv.lshBlock );
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.lshBlock);
        }

        /**/
    }
}
