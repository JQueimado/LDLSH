package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import static SystemLayer.Data.ErasureCodesImpl.ErasureCodes.*;

public interface MultiMap {
    //Inserts data into the MultiMap
    void insert(
            LSHHash lshHash,
            UniqueIdentifier uniqueIdentifier,
            ErasureCodes erasureCodes
    );

    //Searches and Returns an ErasureBlock
    byte[] complete(
            LSHHash lshHash,
            UniqueIdentifier uniqueIdentifier
    );

    //All results for a given lshHash
    MultiMapValue[] query( LSHHash lshHash );

    //Extras
    void setHashBlockPosition(int position);
    int getHashBlockPosition();

    void setTotalBlocks( int totalBlocks );
    int getTotalBlocks();

    public record MultiMapValue(
            LSHHash lshHash,
            UniqueIdentifier uniqueIdentifier,
            ErasureBlock ErasureCode
    ){/**/}
}
