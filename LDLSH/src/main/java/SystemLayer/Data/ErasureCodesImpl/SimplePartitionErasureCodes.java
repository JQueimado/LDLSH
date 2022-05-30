package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.IncompleteBlockException;

import java.util.ArrayList;
import java.util.List;

public class SimplePartitionErasureCodes extends ErasureCodesImpl {

    public SimplePartitionErasureCodes(DataContainer context) {
        super( context );
    }

    @Override
    public void encodeDataObject(byte[] object, int n_blocks) {
        super.total_blocks = n_blocks;
        erasureBlocks = new ErasureBlock[n_blocks];

        int block_length = object.length / n_blocks + 1;

        /*
        byte[] current_block = new byte[block_length];
        int c = 0;
        for ( int i = 0; i<data.length; i++ ){
            byte b = data[i];
            current_block[c] = b;
            c++;

            if( (c >= block_length) || (i == data.length-1) ){
                ErasureBlock erasureBlock = new ErasureBlock(current_block, erasureBlocks.size());
                current_block = new byte[block_length];
                c = 0;

                erasureBlocks.add( erasureBlock );
            }else {

            }
        }
        */

        int c = 0;
        for( int i = 0; i<n_blocks; i++ ){
            byte[] block;
            if( object.length - c > block_length )
                block = new byte[block_length];
            else
                block = new byte[ object.length -c ];
            int bc = 0;

            while (bc<block.length){
                block[bc] = object[c];
                c++;
                bc++;
            }

            ErasureBlock erasureBlock = new ErasureBlock( block, i );
            erasureBlocks[i] = erasureBlock;
        }

        number_of_blocks = n_blocks;

        //DEBUG
        /*
        for( ErasureBlock erasureBlock : erasureBlocks ){
            for( byte b : erasureBlock.block_data() ){
                System.out.print( String.valueOf(b) + " " );
            }
            System.out.print("\n");
        }
        */
    }

    @Override
    public byte[] decodeDataObject()
            throws IncompleteBlockException {

        if(number_of_blocks < super.total_blocks)
            throw new IncompleteBlockException();

        List<Byte> raw_data = new ArrayList<>();
        for( ErasureBlock erasureBlock : erasureBlocks ){
            for( byte b : erasureBlock.block_data() )
                raw_data.add(b);
        }

        byte[] data = new byte[raw_data.size()];
        for( int i = 0; i<raw_data.size(); i++){
            data[i] = raw_data.get(i);
        }

        return data;
    }

}
