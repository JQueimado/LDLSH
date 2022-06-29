package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHashImpl;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public abstract class MultiMapImpl implements MultiMap{

    private final String hash_position_config = "HASH_POSITION";
    private final String number_hashes_config = "TOTAL_HASH";

    private DataContainer appContext;

    private int hash_position;
    private int total_hash_blocks;

    public MultiMapImpl( DataContainer appContext ){
        this.appContext = appContext;
    }

    @Override
    public LSHHashImpl.LSHHashBlock getBlock(LSHHash hash) {
        return hash.getBlockAt(hash_position);
    }

    @Override
    public abstract void insert(
            LSHHash lshHash,
            UniqueIdentifier uniqueIdentifier,
            ErasureBlock erasureBlock
    );

    @Override
    public abstract ErasureBlock complete(LSHHash lshHash, UniqueIdentifier uniqueIdentifier);

    @Override
    public abstract MultiMapValue[] query(LSHHashImpl.LSHHashBlock lshHash);

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
