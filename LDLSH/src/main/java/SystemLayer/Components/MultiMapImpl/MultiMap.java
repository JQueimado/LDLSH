package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHash.LSHHashBlock;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.*;

public interface MultiMap {

    //Finds and returns a reference to the LSHHashBlock stored in the multimap that corresponds to the given LSHHash
    LSHHashBlock getBlock(LSHHash hash );

    //Inserts data into the MultiMap
    void insert(
            LSHHash lshHash,
            UniqueIdentifier uniqueIdentifier,
            ErasureCodes erasureCodes
    );

    //Searches and Returns an ErasureBlock
    ErasureCodesImpl.ErasureBlock complete(
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
