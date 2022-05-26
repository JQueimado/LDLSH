package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.io.Serializable;

public interface ErasureCodes extends Serializable, Comparable<ErasureCodes> {
    /**
     * Encodes a given data object
     * @param data object subject to encoding
     * @param n_blocks total number of data objects
     */
    void encodeDataObject( byte[] data, int n_blocks );

    /**
     * Decodes the stored erasure codes into a given data object.
     * @return object destination populated with the decoded data.
     * @throws ErasureCodesImpl.IncompleteBlockException thrown when the number of store erasure codes is not
     * sufficient to generate the data object.
     * @throws ErasureCodesImpl.CorruptBlockException thrown when the validation process fails.
     */
    byte[] decodeDataObject()
            throws ErasureCodesImpl.IncompleteBlockException, ErasureCodesImpl.CorruptBlockException;

    /**
     * Adds an Erasure block to its predefined position
     * @param erasureBlock block to be added
     */
    void addBlockAt( ErasureCodesImpl.ErasureBlock erasureBlock );

    /**
     * Returns all stored blocks
     * @return and Array containing all stored blocks in their respective positions
     */
    ErasureCodesImpl.ErasureBlock[] getErasureBlocks();

    /**
     * Returns a single block from a given position
     * @param position the pretended block's position
     * @return the pretended block
     */
    ErasureCodesImpl.ErasureBlock getBlockAt(int position );
}
