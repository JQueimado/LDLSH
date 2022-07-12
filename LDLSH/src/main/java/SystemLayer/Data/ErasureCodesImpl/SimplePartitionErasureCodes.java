package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.IncompleteBlockException;
import io.netty.buffer.ByteBuf;

public class SimplePartitionErasureCodes extends ErasureCodesImpl {

    public SimplePartitionErasureCodes(DataContainer context) {
        super( context );
    }

    @Override
    public void encodeDataObject(byte[] object, int n_blocks) throws Exception {
        total_blocks = n_blocks;
        number_of_blocks = n_blocks;
        erasureBlocks = new ErasureBlock[n_blocks];

        //Add 1 byte for padding value
        object = addPadding( object, total_blocks );

        int block_length = object.length / n_blocks;
        //Create Blocks
        for( int i = 0; i<n_blocks; i++ ){
            byte[] block = new byte[block_length];
            ErasureBlock erasureBlock = new ErasureBlock( block, i );
            erasureBlocks[i] = erasureBlock;
        }

        //Copy data
        int c=0;
        for (int i=0; i<block_length; i++){
            for (ErasureBlock erasureBlock : erasureBlocks) {
                erasureBlock.block_data()[i] = object[c];
                c++;
            }
        }
    }

    @Override
    public byte[] decodeDataObject() throws IncompleteBlockException {

        if(number_of_blocks < super.total_blocks)
            throw new IncompleteBlockException();

        int block_size = erasureBlocks[0].block_data().length; //All blocks have the same length
        int data_size = erasureBlocks.length * block_size;
        byte[] data = new byte[ data_size ];

        int c=0;
        for(int i=0; i<block_size; i++){
            for (ErasureBlock erasureBlock : erasureBlocks){
                data[c] = erasureBlock.block_data()[i];
                c++;
            }
        }

        //padding
        data = removePadding( data );
        return data;
    }

}
