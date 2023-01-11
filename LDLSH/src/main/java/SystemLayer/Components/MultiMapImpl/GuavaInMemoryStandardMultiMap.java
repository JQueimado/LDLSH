package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.LSHHashBlock;
import SystemLayer.Data.DataUnits.ModelMultimapValue;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.InvalidMapValueTypeException;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Arrays;
import java.util.Collection;

public class GuavaInMemoryStandardMultiMap extends MultiMapImpl{

    private final Multimap<LSHHashBlock, MultiMapValue> multiMap;

    //Constructors
    public GuavaInMemoryStandardMultiMap(int hash_position, int total_hash_blocks, DataContainer appContext){
        this(appContext);
        setHashBlockPosition(hash_position);
        setTotalBlocks(total_hash_blocks);
    }

    public GuavaInMemoryStandardMultiMap(DataContainer appContext){
        super(appContext);
        this.multiMap = HashMultimap.create();
    }

    @Override
    public LSHHashBlock getBlock(LSHHash hash) {
        LSHHashBlock rcv_block = hash.getBlockAt(hash_position);

        for ( LSHHashBlock current : multiMap.keys() ){
            if(Arrays.hashCode(current.lshBlock()) == Arrays.hashCode(rcv_block.lshBlock())){
                return current;
            }
        }

        return null;
    }

    @Override
    public synchronized boolean insert(LSHHash lshHash, MultiMapValue value) {
        //Insert Values
        multiMap.put( lshHash.getBlockAt(hash_position), value );
        return true;
    }

    @Override
    public ErasureBlock complete( LSHHash lshHash , UniqueIdentifier uniqueIdentifier) throws InvalidMapValueTypeException {
        Collection<MultiMapValue> multiMapValues = multiMap.get(lshHash.getBlockAt(hash_position));

        for( MultiMapValue rawMultiMapValue: multiMapValues ){
            ModelMultimapValue multiMapValue;

            try{
                multiMapValue = (ModelMultimapValue) rawMultiMapValue;
            }catch (Exception e){
                throw new InvalidMapValueTypeException( "Multimap returned a non completable map value" );
            }

            if( uniqueIdentifier.compareTo( multiMapValue.uniqueIdentifier() ) == 0 ){
                return multiMapValue.erasureCode();
            }
        }
        return null;
    }

    @Override
    public MultiMapValue[] query(LSHHashBlock lshHash) {
        Collection<MultiMapValue> collection = multiMap.get( lshHash );
        MultiMapValue[] result = new MultiMapValue[collection.size()];
        collection.toArray(result);
        return result;
    }
}
