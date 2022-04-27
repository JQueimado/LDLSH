package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplePartitionErasureCodes implements ErasureCodes {

    private List<ErasureBlock> erasureBlocks;
    private int minimum_blocks;

    @Override
    public void encodeDataObject(DataObject dataObject, int n_blocks) {
        minimum_blocks = n_blocks;
        erasureBlocks = new ArrayList<>();

        byte[] data = dataObject.toByteArray();
        int block_length = data.length / n_blocks + 1;

        byte[] current_block = new byte[block_length];
        int c = 0;
        for ( int i = 0; i<data.length; i++ ){
            byte b = data[i];
            current_block[c] = b;
            c++;
            if( (c >= block_length) || (i == data.length-1) ){
                ErasureBlock erasureBlock = new ErasureBlock(current_block, c);
                current_block = new byte[block_length];
                c = 0;

                erasureBlocks.add( erasureBlock );
            }
        }

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
    public DataObject decodeDataObject(DataObject object) throws IncompleteBlockException {

        if(erasureBlocks.size() < minimum_blocks)
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
        return object;
    }

    @Override
    public void addBlockAt(ErasureBlock erasureBlock) {
        erasureBlocks.add( erasureBlock.position(), erasureBlock );
    }

    @Override
    public ErasureBlock[] getErasureBlocks() {
        return erasureBlocks.toArray(new ErasureBlock[erasureBlocks.size()]);
    }

    @Override
    public ErasureBlock getBlockAt(int blocks) {
        return erasureBlocks.get(blocks);
    }

    @Override
    public int compareTo(ErasureCodes o) {
        for ( int i = 0; i<erasureBlocks.size(); i++ ){
            ErasureBlock A_block = erasureBlocks.get(i);
            ErasureBlock B_block = o.getBlockAt(i);

            if( A_block == null && B_block != null )
                return -1;

            if( B_block == null && A_block != null )
                return -1;

            int r = Arrays.compare(A_block.block_data(), B_block.block_data());
            if( r != 0 )
                return -1;
        }
        return 0;
    }
}
