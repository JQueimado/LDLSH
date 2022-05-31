package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.ErasureCodesImpl.BlackblazeReedSolomonErasureCodesLib.*;
import SystemLayer.SystemExceptions.IncompleteBlockException;

public class BlackblazeReedSolomonErasureCodes extends ErasureCodesImpl{

    //Cada erasure code block contem um bloco de chave e um blocco de dados

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
        int c = 0;
        for ( int i=0; i<shard_size; i++ ) {
            for (int j = 0; j < k; j++) {
                shards[j][i] = data[c];
                c++;
            }
        }

        return shards;
    }

    public static byte[] shardsToByteArray( byte[][] shards, int data_size ){
        byte[] data = new byte[data_size];
        int c = 0;
        for (int i=0; i<shards[0].length; i++) {
            for (int j = 0; j < k; j++) {
                data[c] = shards[j][i];
                c++;
            }
        }
        return data;
    }

    //Codes
    boolean[] isPresent;
    int block_size;

    public BlackblazeReedSolomonErasureCodes( DataContainer appContext){
        super( appContext );
        block_size = -1;

        isPresent = new boolean[n];
        for (int c = 0; c<n; c++){
            isPresent[c] = false;
        }
    }

    @Override
    public void encodeDataObject(byte[] object, int n_blocks) {
        byte[][] shards = byteArrayToShards(object);
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
        number_of_blocks = n_blocks;
    }

    @Override
    public byte[] decodeDataObject()
            throws IncompleteBlockException {

        if( number_of_blocks < n-t ){
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
        return shardsToByteArray(matrix, data_size);
    }

    @Override
    public void addBlockAt(ErasureBlock erasureBlock) {
        int pos = erasureBlock.position();
        if( !isPresent[pos] )
            number_of_blocks++;
        if( block_size ==-1 )
            block_size = erasureBlock.block_data().length;
        erasureBlocks[pos] = erasureBlock;
        isPresent[pos] = true;
    }
}
