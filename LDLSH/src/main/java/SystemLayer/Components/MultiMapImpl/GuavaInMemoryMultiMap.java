package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;

import static SystemLayer.Data.LSHHashImpl.LSHHash.*;

public class GuavaInMemoryMultiMap implements MultiMap{

    private final Multimap<LSHHashBlock, MultiMapValue> multiMap;
    private int hash_position;
    private int total_hash_blocks;

    //Constructors
    public GuavaInMemoryMultiMap(int hash_position, int total_hash_blocks){
        this.multiMap = ArrayListMultimap.create();
        setHashBlockPosition(hash_position);
        setTotalBlocks(total_hash_blocks);
    }

    @Override
    public void insert(LSHHash lshHash, UniqueIdentifier uniqueIdentifier, ErasureCodes erasureCodes) {
        //Build value
        MultiMapValue mapValue = new MultiMapValue(
                lshHash,
                uniqueIdentifier,
                erasureCodes.getBlockAt(hash_position)
        );

        //Insert Values
        multiMap.put( lshHash.getBlockAt(hash_position), mapValue );
    }

    @Override
    public byte[] complete(LSHHash lshHash, UniqueIdentifier uniqueIdentifier) {
        return new byte[0];
    }

    @Override
    public MultiMapValue[] query(LSHHash lshHash) {
        Collection<MultiMapValue> collection = multiMap.get( lshHash.getBlockAt(hash_position) );
        MultiMapValue[] result = new MultiMapValue[collection.size()];
        collection.toArray(result);
        return result;
    }

    @Override
    public void setHashBlockPosition(int position) {
        this.hash_position = position;
    }

    @Override
    public int getHashBlockPosition() {
        return hash_position;
    }

    @Override
    public void setTotalBlocks(int totalBlocks) {
        this.total_hash_blocks = totalBlocks;
    }

    @Override
    public int getTotalBlocks() {
        return total_hash_blocks;
    }
}