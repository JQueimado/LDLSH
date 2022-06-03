package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.IncompleteBlockException;
import org.jetbrains.annotations.NotNull;

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
    public int compareTo( @NotNull ErasureCodes o) {
        for ( int i = 0; i<erasureBlocks.length; i++ ){
            ErasureBlock A_block = erasureBlocks[i];
            ErasureBlock B_block = o.getBlockAt(i);

            if( A_block == null || B_block == null )
                return -1;

            if( !A_block.equals(B_block))
                return -1;
        }
        return 0;
    }

    @Override
    public boolean equals( @NotNull Object obj) {
        try {
            ErasureCodesImpl temp = (ErasureCodesImpl) obj;
            return this.compareTo(temp) == 0;
        }catch (Exception e){
            return false;
        }
    }

    //Auxiliary classes
    /**
     * Adds or removes zero padding from a given byte array
     * @param data input data
     * @param size if grater than the data size, add padding. if smaller than data size, removes padding
     * @return new processed byte array
     */
    public static byte[] padding( byte[] data, int size ){
        byte[] result = new byte[size];
        System.arraycopy(data, 0, result, 0, Math.min(data.length, size));
        return result;
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
