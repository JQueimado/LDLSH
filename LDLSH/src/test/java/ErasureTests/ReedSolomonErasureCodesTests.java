package ErasureTests;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.ErasureCodesImpl.BlackblazeReedSolomonErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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

        DataObject<String> string_data = new StringDataObject("This is a string to be processed char by char.");

        //Encode
        BlackblazeReedSolomonErasureCodes codes = new BlackblazeReedSolomonErasureCodes();
        codes.encodeDataObject( string_data, 6);

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

    }
}
