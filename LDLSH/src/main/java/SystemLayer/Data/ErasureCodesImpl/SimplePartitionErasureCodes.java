package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.util.ArrayList;
import java.util.List;

public class SimplePartitionErasureCodes extends ErasureCodesImpl {

    private static final String total_blocks = "N_BANDS";

    public SimplePartitionErasureCodes(DataContainer context) throws Exception {
        super(context, Integer.parseInt( context.getConfigurator().getConfig(total_blocks) ));
    }

    @Override
    public void encodeDataObject(DataObject dataObject, int n_blocks) {
        super.total_blocks = n_blocks;
        erasureBlocks = new ErasureBlock[n_blocks];

        byte[] data = dataObject.toByteArray();
        int block_length = data.length / n_blocks + 1;

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
            if( data.length - c > block_length )
                block = new byte[block_length];
            else
                block = new byte[ data.length -c ];
            int bc = 0;

            while (bc<block.length){
                block[bc] = data[c];
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
    public DataObject decodeDataObject(DataObject object, UniqueIdentifier validation_identifier)
            throws IncompleteBlockException, CorruptBlockException {

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

        object.setByteArray(data);
        return super.decodeDataObject(object, validation_identifier);
    }

}
