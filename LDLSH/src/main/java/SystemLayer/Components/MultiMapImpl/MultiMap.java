package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Data.DataUnits.ErasureBlock;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.DataUnits.LSHHashBlock;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import static SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.*;

public interface MultiMap {

    //Finds and returns a reference to the LSHHashBlock stored in the multimap that corresponds to the given LSHHash
    LSHHashBlock getBlock(LSHHash hash );

    //Inserts data into the MultiMap
    boolean insert(
            LSHHash lshHash,
            MultiMapValue value
    ) throws Exception;

    //Searches and Returns an ErasureBlock
    ErasureBlock complete(
            LSHHash lshHash,
            UniqueIdentifier uniqueIdentifier
    ) throws Exception;

    //All results for a given lshHash
    MultiMapValue[] query(LSHHashBlock lshHash ) throws Exception ;

    //Extras
    void setHashBlockPosition(int position);
    int getHashBlockPosition();

    void setTotalBlocks( int totalBlocks );

    int getTotalBlocks();

}
