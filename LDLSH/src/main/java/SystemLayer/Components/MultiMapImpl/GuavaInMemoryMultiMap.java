package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.ModelMultimapValue;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.DataUnits.LSHHashBlock;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.InvalidMapValueTypeException;
import com.google.common.collect.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.SortedSet;

public class GuavaInMemoryMultiMap extends MultiMapImpl{

    private final SortedSetMultimap<LSHHashBlock, MultiMapValue> multiMap;

    //Constructors
    public GuavaInMemoryMultiMap(int hash_position, int total_hash_blocks, DataContainer appContext){
        this(appContext);
        setHashBlockPosition(hash_position);
        setTotalBlocks(total_hash_blocks);
    }

    public GuavaInMemoryMultiMap(DataContainer appContext){
        super(appContext);
        this.multiMap = TreeMultimap.create();
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
    public boolean insert(LSHHash lshHash, MultiMapValue value) {
        //Insert Values
        synchronized (multiMap) {
            multiMap.put(lshHash.getBlockAt(hash_position), value);
        }
        return true;
    }

    @Override
    public ErasureBlock complete(
            LSHHash lshHash ,
            UniqueIdentifier uniqueIdentifier)
    throws InvalidMapValueTypeException {
        SortedSet<MultiMapValue> multiMapValuesSet = multiMap.get(lshHash.getBlockAt(hash_position));
        MultiMapValue[] multiMapValuesArray = multiMapValuesSet.toArray(new MultiMapValue[0]);

        ModelMultimapValue comparisonAgent = new ModelMultimapValue(lshHash, uniqueIdentifier, null);

        int low = 0;
        int high = multiMapValuesArray.length - 1;
        int pivot;
        while( high >= low ){
            pivot = (low + high)/2;

            MultiMapValue rawMultiMapValue = multiMapValuesArray[pivot];
            ModelMultimapValue multiMapValue;

            try{
                multiMapValue = (ModelMultimapValue) rawMultiMapValue;
            }catch (Exception e){
                throw new InvalidMapValueTypeException( "Multimap returned a non completable map value" );
            }

            int compare = comparisonAgent.compareTo(multiMapValue);
            if( compare == 0 )
                return multiMapValue.erasureCode();
            else if (compare < 0) {
                high = pivot - 1;
            }else{
                low = pivot + 1;
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
