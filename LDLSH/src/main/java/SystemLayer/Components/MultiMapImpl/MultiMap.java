package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public interface MultiMap {
    //Inserts data into the MultiMap
    void insert(
            byte[] lshHash,
            byte[] uniqueIdentifier,
            byte[] erasureCodes
    );

    //Searches and Returns an ErasureBlock
    byte[] complete(
            byte[] lshHash,
            byte[] uniqueIdentifier
    );

    //All results for a given lshHash
    MultiMapValue[] query( byte[] lshHash );

    //Extras
    void setHashBlockPosition(int position);
    int getHashBlockPosition();

    void setTotalBlocks( int totalBlocks );
    int getTotalBlocks();

    public record MultiMapValue(
            byte[] lshHash,
            byte[] uniqueIdentifier,
            byte[] ErasureCode
    ){/**/}
}
