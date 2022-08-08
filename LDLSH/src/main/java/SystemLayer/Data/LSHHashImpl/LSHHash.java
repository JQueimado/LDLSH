package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Data.DataUnits.LSHHashBlock;

import java.io.Serializable;

public interface LSHHash extends Serializable, Comparable<LSHHash> {

    /**
     * Sets a given object to associate with a resulting signature
     * @param object object to be associated
     * @param n_blocks number of divisions on the signature
     */
    void setObject( byte[] object, int n_blocks );

    /**
     * Returns the generated complete signature
     * @return a bite array containing the associated object's signature
     */
    byte[] getSignature();

    /**
     * Returns the list of signature blocks
     * @return Array of signature blocks
     */
    LSHHashBlock[] getBlocks();

    /**
     * Returns a block associated with a given position
     * @param position pretended block's position
     * @return pretended block
     */
    LSHHashBlock getBlockAt(int position );

}
