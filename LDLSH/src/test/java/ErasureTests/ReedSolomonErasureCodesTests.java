package ErasureTests;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.ErasureCodesImpl.BlackblazeReedSolomonErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static java.lang.System.in;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReedSolomonErasureCodesTests {


    @Test
    public void shardingTest() {
        //Setup
        BlackblazeReedSolomonErasureCodes.setupEncoder( 6, 4 );

        DataObject<String> string_data = new StringDataObject("This is a string to be processed char by char.");
        byte[][] sharding = BlackblazeReedSolomonErasureCodes.byteArrayToShards(string_data.toByteArray() );

        for( byte[] row:sharding )
            System.out.println( Arrays.toString(row) );
    }

    @Test
    public void encodeDecodeTest() throws Exception {
        //Setup
        BlackblazeReedSolomonErasureCodes.setupEncoder( 6, 4 );

        DataObject<String> object1 = new StringDataObject("This is a string to be processed char by char.");

        //Encode
        BlackblazeReedSolomonErasureCodes codes = new BlackblazeReedSolomonErasureCodes();
        codes.encodeDataObject( object1, 6);

        ErasureCodes.ErasureBlock[] blocks = codes.getErasureBlocks();
        for( ErasureCodes.ErasureBlock row: blocks )
            System.out.println( Arrays.toString(row.block_data()) );

        //Decode
        DataObject object2 = new StringDataObject();
        BlackblazeReedSolomonErasureCodes codes2 = new BlackblazeReedSolomonErasureCodes();

        for(ErasureCodes.ErasureBlock block: blocks){
            codes2.addBlockAt(block);
        }

        codes2.decodeDataObject( object2 );
        System.out.println(object2.getValues());

        assertEquals(object1.getValues(), object2.getValues() );
    }

    @Test
    public void randomPopMaxTest(){
        //Setup
        int k = 4;
        int n = 6;
        int t = n-k;
        BlackblazeReedSolomonErasureCodes.setupEncoder( n, k );

        DataObject<String> string_data = new StringDataObject("This is a string to be processed char by char.");

        //Encode
        BlackblazeReedSolomonErasureCodes codes1 = new BlackblazeReedSolomonErasureCodes();
        codes1.encodeDataObject( string_data, 6);

        ErasureCodes.ErasureBlock[] blocks = codes1.getErasureBlocks();
        System.out.println("Encoded blocks");
        for( ErasureCodes.ErasureBlock row: blocks )
            System.out.println( Arrays.toString(row.block_data()) );

        //Decode and remove
        BlackblazeReedSolomonErasureCodes codes2 = new BlackblazeReedSolomonErasureCodes();

        //Randomly slects t rows to be removed
        Random random = new Random();
        int[] missing_rows = new int[t];

        for( int c=0; c<t; c++ ){
            missing_rows[c] = random.nextInt(n);
        }

    }
}
