package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.BlackblazeReedSolomonErasureCodesLib.*;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class BlackblazeReedSolomonErasureCodes extends ErasureCodesImpl{

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
    boolean[] isPresent;
    int block_size;

    public BlackblazeReedSolomonErasureCodes(DataContainer appContext){
        super(appContext, n);
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
    public DataObject decodeDataObject(DataObject object, UniqueIdentifier validation_identifier)
            throws IncompleteBlockException, CorruptBlockException {

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
        byte[] data = shardsToByteArray(matrix, data_size);
        object.setByteArray(data);
        return super.decodeDataObject(object, validation_identifier);
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
