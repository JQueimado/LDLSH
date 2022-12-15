package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataUnits.ErasureBlock;
import SystemLayer.SystemExceptions.IncompleteBlockException;

import java.io.Serializable;

public interface ErasureCodes extends Serializable, Comparable<ErasureCodes> {
    /**
     * Encodes a given data object
     * @param data object subject to encoding
     * @param n_blocks total number of data objects
     */
    void encodeDataObject( byte[] data, int n_blocks ) throws Exception ;

    /**
     * Decodes the stored erasure codes into a given data object.
     * @return object destination populated with the decoded data.
     * @throws IncompleteBlockException thrown when the number of store erasure codes is not
     * sufficient to generate the data object.
     */
    byte[] decodeDataObject() throws IncompleteBlockException;

    /**
     * Adds an Erasure block to its predefined position
     * @param erasureBlock block to be added
     */
    void addBlockAt( ErasureBlock erasureBlock );

    /**
     * Returns all stored blocks
     * @return and Array containing all stored blocks in their respective positions
     */
    ErasureBlock[] getErasureBlocks();

    /**
     * Returns a single block from a given position
     * @param position the pretended block's position
     * @return the pretended block
     */
    ErasureBlock getBlockAt(int position );
}
