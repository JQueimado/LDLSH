package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.BlackblazeReedSolomonErasureCodesLib.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlackblazeReedSolomonErasureCodes implements ErasureCodes{

    //Encoders
    private static int n; //Total blocks
    private static int k; //Data blocks
    private static int t; //Parity blocks

    private static ReedSolomon encoder;

    public static void setupEncoder( int n, int k ){
        BlackblazeReedSolomonErasureCodes.n = n;
        BlackblazeReedSolomonErasureCodes.k = k;
        BlackblazeReedSolomonErasureCodes.t = n-k;
        encoder = ReedSolomon.create(k,t);
    }

    public static byte[][] byteArrayToShards(byte[] data ){
        int shard_size = data.length/k;
        byte[][] shards = new byte[n][shard_size];

        //Copy data to matrix
        for ( int i=0; i<k; i++ )
           System.arraycopy(data, i*shard_size, shards[i], 0, shard_size);

        return shards;
    }

    public static byte[] shardsToByteArray( byte[][] shards, int data_size ){
        byte[] data = new byte[data_size];
        for(int y = 0; y< k; y++){
            int block_size = shards[y].length;
            System.arraycopy(shards[y], 0, data, y*block_size, block_size);
        }
        return data;
    }

    //Codes
    ErasureBlock[] erasureBlocks;
    boolean[] isPresent;
    int n_blocks;
    int block_size;

    public BlackblazeReedSolomonErasureCodes(){
        erasureBlocks = new ErasureBlock[n];
        n_blocks = 0;
        block_size = -1;

        isPresent = new boolean[n];
        for (int c = 0; c<n; c++){
            isPresent[c] = false;
        }
    }

    @Override
    public void encodeDataObject(DataObject dataObject, int n_blocks) {
        byte[] data = dataObject.toByteArray();
        byte[][] shards = byteArrayToShards(data);
        encoder.encodeParity(shards, 0, shards[0].length);

        //Blocks
        erasureBlocks = new ErasureBlock[n];
        int c = 0;
        //Copy data
        for( byte[] block : shards ){
            erasureBlocks[c] = new ErasureBlock(block, c);
            isPresent[c] = true;
            c++;
        }
    }

    @Override
    public DataObject decodeDataObject(DataObject object) throws IncompleteBlockException {

        if( n_blocks < n-t ){
            throw new IncompleteBlockException();
        }

        //Calculate missing and
        //Assemble shards
        byte[][] matrix = new byte[n][];
        for( int c = 0; c<n; c++ ){
            ErasureBlock current = erasureBlocks[c];
            if( current == null ){
                matrix[c] = new byte[block_size];
            }else {
                matrix[c] = current.block_data();
            }
        }

        encoder.decodeMissing(matrix,isPresent,0,block_size);

        int data_size = k*block_size;
        byte[] data = shardsToByteArray(matrix, data_size);
        object.setByteArray(data);
        return object;
    }

    @Override
    public void addBlockAt(ErasureBlock erasureBlock) {
        int pos = erasureBlock.position();
        if( !isPresent[pos] )
            n_blocks++;
        if( block_size ==-1 )
            block_size = erasureBlock.block_data().length;
        erasureBlocks[pos] = erasureBlock;
        isPresent[pos] = true;
    }

    @Override
    public ErasureBlock[] getErasureBlocks() {
        return erasureBlocks;
    }

    @Override
    public ErasureBlock getBlockAt(int blocks) {
        return erasureBlocks[blocks];
    }

    @Override
    public int compareTo(ErasureCodes o) {
        return Arrays.compare( getErasureBlocks(), o.getErasureBlocks() ) ;
    }

}
