package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.IncompleteBlockException;

import java.util.Arrays;

public abstract class ErasureCodesImpl implements ErasureCodes{

    public DataContainer appContext;
    public ErasureBlock[] erasureBlocks;
    public int number_of_blocks;
    public int total_blocks;

    /**
     * Erasure codes super constructor
     * @param appContext application context
     */
    public ErasureCodesImpl( DataContainer appContext ){
        this.appContext = appContext;
        this.total_blocks = appContext.getNumberOfBands();
        this.erasureBlocks = new ErasureBlock[total_blocks];
        this.number_of_blocks = 0;
    }

    //Abstract methods
    @Override
    public abstract void encodeDataObject(byte[] object, int n_blocks);

    @Override
    public abstract byte[] decodeDataObject() throws IncompleteBlockException;

    //Standard methods
    @Override
    public void addBlockAt(ErasureBlock erasureBlock) {
        if( erasureBlocks[erasureBlock.position()] == null )
            number_of_blocks++;
        erasureBlocks[erasureBlock.position()] = erasureBlock;
    }

    @Override
    public ErasureBlock[] getErasureBlocks() {
        return erasureBlocks;
    }

    @Override
    public ErasureBlock getBlockAt(int position) {
        return erasureBlocks[position];
    }

    @Override
    public int compareTo( ErasureCodes o) {
        for ( int i = 0; i<erasureBlocks.length; i++ ){
            ErasureBlock A_block = erasureBlocks[i];
            ErasureBlock B_block = o.getBlockAt(i);

            if( A_block == null && B_block != null )
                return -1;

            if( B_block == null && A_block != null )
                return -1;

            if( !A_block.equals(B_block))
                return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        ErasureCodesImpl temp = (ErasureCodesImpl) obj;
        return this.compareTo(temp) == 0;
    }

    //Subclasses
    /**
     * Object representing a single Erasure code
     * @param block_data erasure code's data
     * @param position erasure code's position
     */
    public record ErasureBlock( byte[] block_data, int position ) implements Comparable<ErasureBlock> {
        @Override
        public int compareTo(ErasureBlock o) {
            if ( position != o.position )
                return -1;
            return Arrays.compare(block_data, o.block_data);
        }

        @Override
        public boolean equals(Object obj) {
            if( obj.getClass() != ErasureBlock.class )
                return false;
            return this.compareTo( (ErasureBlock) obj ) == 0 ;
        }
    }

}
