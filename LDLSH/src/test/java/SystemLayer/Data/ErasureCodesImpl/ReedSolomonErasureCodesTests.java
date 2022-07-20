package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReedSolomonErasureCodesTests {

    private int n, k, t;
    private StringDataObject string_data;
    private DataContainer appContext;

    @BeforeEach
    public void beforeEach(){
        //Encoder config
        n = 6;
        k = 4;
        t = n-k;

        //Context
        appContext = new DataContainer("");
        appContext.getConfigurator().setConfig("OBJECT_TYPE", "STRING");
        appContext.getConfigurator().setConfig("ERASURE_CODES", "REED_SOLOMON");
        appContext.getConfigurator().setConfig("UNIQUE_IDENTIFIER", "SHA256");

        appContext.getConfigurator().setConfig("N_BANDS", "%d".formatted(n));
        appContext.getConfigurator().setConfig("ERASURE_FAULTS", "%d".formatted(t));

        //Data config
        String value = "This is a string to be processed char by char";
        string_data = (StringDataObject) appContext.getDataObjectFactory().getNewDataObject();
        string_data.setValues(value);
        appContext.getConfigurator().setConfig("VECTOR_SIZE", "%d".formatted(value.length()));

        appContext.getConfigurator().setConfig(
                "VECTOR_SIZE",
                "%d".formatted(string_data.toByteArray().length)
        );
    }

    /**
     * Tests the sharding function by comparing its result to a handmade one
     */
    @Test
    public void shardingTest() throws Exception {
        BackblazeReedSolomonErasureCodes.setupEncoder( appContext );

        byte[] test_data = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};

        byte[][] expected_data = new byte[][]{
                new byte[]{1,5,9,13},
                new byte[]{2,6,10,14},
                new byte[]{3,7,11,15},
                new byte[]{4,8,12,16},
                new byte[]{0,0,0,0},
                new byte[]{0,0,0,0}
        };

        byte[][] sharded_data = BackblazeReedSolomonErasureCodes.byteArrayToShards(test_data);

        for(int i=0; i<n; i++){
            assertArrayEquals(expected_data[i], sharded_data[i]);
        }
    }

    /**
     * Tests the reverse sharding by comparing its result to a handmade one
     */
    @Test
    public void reverseSharding() throws Exception{
        BackblazeReedSolomonErasureCodes.setupEncoder( appContext );

        byte[][] test_data = new byte[][]{
                new byte[]{1,5,9,13},
                new byte[]{2,6,10,14},
                new byte[]{3,7,11,15},
                new byte[]{4,8,12,16},
                new byte[]{0,0,0,0},
                new byte[]{0,0,0,0}
        };

        byte[] expected_data = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};

        byte[] result_data = BackblazeReedSolomonErasureCodes.shardsToByteArray(test_data, expected_data.length);

        assertArrayEquals(expected_data, result_data);
    }

    /**
     * Test the encoding and decoding process by encoding a data object, display the result and decode said result into
     * a new data object in order to compare the original object to the resulting object.
     * @throws Exception
     */
    @Test
    public void encodeDecodeCompleteTest() throws Exception {
        //Encode
        BackblazeReedSolomonErasureCodes codes =
                (BackblazeReedSolomonErasureCodes) appContext.getErasureCodesFactory().getNewErasureCodes();
        codes.encodeDataObject( string_data.toByteArray(), n);
        printBlocks(codes.getErasureBlocks());

        //Decode
        DataObject<String> result_object = appContext.getDataObjectFactory().getNewDataObject();
        BackblazeReedSolomonErasureCodes codes2 =
                (BackblazeReedSolomonErasureCodes) appContext.getErasureCodesFactory().getNewErasureCodes();

        for(ErasureBlock block: codes.getErasureBlocks()){
            codes2.addBlockAt(block);
        }

        byte[] tempData = codes2.decodeDataObject();
        result_object.setByteArray(tempData);

        //Assertions
        assertEquals(string_data.getValues(), result_object.getValues() );
    }

    /**
     * Tests the decoding functions in an environment where t codes are removed.
     * The removed codes are randomly selected.
     * @throws Exception
     */
    @Test
    public void randomPopMaxTest() throws Exception{
        //Encode
        BackblazeReedSolomonErasureCodes codes1 = new BackblazeReedSolomonErasureCodes(appContext);
        codes1.encodeDataObject( string_data.toByteArray(), n);

        ErasureBlock[] blocks = codes1.getErasureBlocks();
        //Display resulting Blocks
        System.out.println("Encoded blocks");
        printBlocks(blocks);

        BackblazeReedSolomonErasureCodes codes2 = new BackblazeReedSolomonErasureCodes(appContext);
        //Randomly selects t rows to be removed
        Random random = new Random();
        List<Integer> missing_rows = new ArrayList<>();
        for( int c=0; c<t; c++ ){
            missing_rows.add( random.nextInt(n) );
        }
        System.out.println("Removed rows");
        System.out.println(missing_rows);

        //Copies some values
        copyAllButSome(codes2, blocks, missing_rows);

        //Display codes2 status
        System.out.println("blocks for decoding");
        ErasureBlock[] blocks2 = codes2.getErasureBlocks();
        printBlocks(blocks2);

        //Decodes
        DataObject<String> result_object = new StringDataObject();
        byte[] tempData = codes2.decodeDataObject();
        result_object.setByteArray( tempData );

        //Assertions
        assertEquals( result_object.getValues(), result_object.getValues() );
    }

    /**
     * Test the decode function face all erasure codes by removing the codes one at a time.
     * The removed code is removed iteratively.
     * @throws Exception
     */
    @Test
    public void PopAllEach() throws Exception{
        //Encode
        BackblazeReedSolomonErasureCodes codes1 =
                (BackblazeReedSolomonErasureCodes) appContext.getErasureCodesFactory().getNewErasureCodes();
        codes1.encodeDataObject( string_data.toByteArray(), n);

        ErasureBlock[] blocks = codes1.getErasureBlocks();
        //Display resulting Blocks
        System.out.println("Encoded blocks");
        printBlocks(blocks);

        BackblazeReedSolomonErasureCodes codes2;
        List<Integer> toRemove;
        DataObject<String> result_object;
        for(int c = 0; c < n; c++){
            //Pick to remove
            toRemove = new ArrayList<>();
            toRemove.add(c); //Picks c
            System.out.printf("Remove code at: %d\n", c);
            //Remove and Decode
            codes2 = new BackblazeReedSolomonErasureCodes(appContext);
            copyAllButSome(codes2, blocks, toRemove); //Copies all but c
            printBlocks(codes2.getErasureBlocks()); //Shows resulting codes
            result_object = new StringDataObject();
            byte[] tempData = codes2.decodeDataObject(); //Decode
            result_object.setByteArray(tempData);

            //Assertions
            assertEquals( string_data.getValues(), result_object.getValues() );
        }
    }

    //Auxiliary methods
    private void printBlocks(ErasureBlock[] blocks){
        for( ErasureBlock row: blocks )
            if (row != null)
                System.out.println( row.position() + ":" + Arrays.toString(row.block_data()) );
    }

    private void copyAllButSome(
            BackblazeReedSolomonErasureCodes dest,
            ErasureBlock[] src,
            List<Integer> some
    ) {
        for( int c = 0; c<n; c++ ){
            if( !some.contains(c) ){
                dest.addBlockAt( src[c] );
            }
        }
    }
}
