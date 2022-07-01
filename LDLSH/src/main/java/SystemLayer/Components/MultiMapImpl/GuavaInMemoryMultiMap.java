package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHashImpl;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import com.google.common.collect.*;

import java.util.Arrays;
import java.util.Collection;

public class GuavaInMemoryMultiMap extends MultiMapImpl{

    private final String hash_position_config = "HASH_POSITION";
    private final String number_hashes_config = "TOTAL_HASH";
    private final Multimap<LSHHashImpl.LSHHashBlock, MultiMapValue> multiMap;
    private int hash_position;
    private int total_hash_blocks;

    //Constructors
    public GuavaInMemoryMultiMap(int hash_position, int total_hash_blocks, DataContainer appContext){
        this(appContext);
        setHashBlockPosition(hash_position);
        setTotalBlocks(total_hash_blocks);
    }

    public GuavaInMemoryMultiMap(DataContainer appContext){
        super(appContext);
        this.multiMap = HashMultimap.create();
    }

    @Override
    public LSHHashImpl.LSHHashBlock getBlock(LSHHash hash) {
        LSHHashImpl.LSHHashBlock rcv_block = hash.getBlockAt(hash_position);

        for ( LSHHashImpl.LSHHashBlock current : multiMap.keys() ){
            if(Arrays.hashCode(current.lshBlock()) == Arrays.hashCode(rcv_block.lshBlock())){
                return current;
            }
        }

        return null;
    }

    @Override
    public void insert(LSHHash lshHash, UniqueIdentifier uniqueIdentifier, ErasureBlock erasureBlock) {
        //Build value
        MultiMapValue mapValue = new MultiMapValue(
                lshHash,
                uniqueIdentifier,
                erasureBlock
        );

        //Insert Values
        multiMap.put( lshHash.getBlockAt(hash_position), mapValue );
    }

    @Override
    public ErasureBlock complete( LSHHash lshHash , UniqueIdentifier uniqueIdentifier) {
        Collection<MultiMapValue> multiMapValues = multiMap.get( lshHash.getBlockAt(hash_position) );

        for( MultiMapValue multiMapValue: multiMapValues ){
            if( uniqueIdentifier.compareTo( multiMapValue.uniqueIdentifier() ) == 0 ){
                return multiMapValue.erasureCode();
            }
        }
        return null;
    }

    @Override
    public MultiMapValue[] query(LSHHashImpl.LSHHashBlock lshHash) {
        Collection<MultiMapValue> collection = multiMap.get( lshHash );
        MultiMapValue[] result = new MultiMapValue[collection.size()];
        collection.toArray(result);
        return result;
    }
}
