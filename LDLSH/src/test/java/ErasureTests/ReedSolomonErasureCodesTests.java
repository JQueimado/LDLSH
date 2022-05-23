package ErasureTests;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.ErasureCodesImpl.BlackblazeReedSolomonErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReedSolomonErasureCodesTests {

    private int n, k, t;
    private DataObject<String> string_data;
    private byte[][] sharding;
    private DataContainer appContext;

    @BeforeEach
    public void beforeEach() throws Exception{
        //Encoder config
        n = 6;
        k = 4;
        t = n-k;
        BlackblazeReedSolomonErasureCodes.setupEncoder(n, k);
        //Data config
        string_data = new StringDataObject("This is a string to be processed"); //size must be dividable by k
        sharding = BlackblazeReedSolomonErasureCodes.byteArrayToShards( string_data.toByteArray() );

        //Context
        appContext = new DataContainer("");
        appContext.getConfigurator().setConfig("UNIQUE_IDENTIFIER", "SHA256");
    }

    /**
     * Tests the Creation of shards by encoding a Data object into a set of shards and compares it to the original data
     */
    @Test
    public void shardingTest() {
        //Prof of sharding
        int c = 0;
        int row_length = sharding[0].length;
        byte[] temp;
        for( byte[] row:sharding ) {
            System.out.println(Arrays.toString(row));
            if( c < k ) {
                //Splits data into an array of the same row size
                temp = new byte[row_length];
                System.arraycopy(string_data.toByteArray(), c * row_length, temp, 0, row_length);
            } else {
              temp = new byte[row_length];
            }
            //Assertions
            assertArrayEquals( temp, row );

            c++; //Increment
        }
    }

    /**
     * Tests the reverse sharding process by creating a set of shards using the previously tested function and reverses
     * the process in order to compare the original object to the object that was sharded and reverse sharded
     */
    @Test
    public void reverseSharding(){
        for( byte[] row:sharding )
            System.out.println(Arrays.toString(row));

        //Revert Sharding
        byte[] reverseSharding = BlackblazeReedSolomonErasureCodes.shardsToByteArray(
                sharding,
                string_data.toByteArray().length
        );

        //Assertions
        assertArrayEquals( string_data.toByteArray(), reverseSharding );
    }

    /**
     * Test the encoding and decoding process by encoding a data object, display the result and decode said result into
     * a new data object in order to compare the original object to the resulting object.
     * @throws Exception
     */
    @Test
    public void encodeDecodeCompleteTest() throws Exception {
        //Encode
        BlackblazeReedSolomonErasureCodes codes = new BlackblazeReedSolomonErasureCodes(appContext);
        codes.encodeDataObject( string_data, n);
        printBlocks(codes.getErasureBlocks());

        //Decode
        DataObject<String> result_object = new StringDataObject();
        BlackblazeReedSolomonErasureCodes codes2 = new BlackblazeReedSolomonErasureCodes(appContext);
        for(ErasureCodesImpl.ErasureBlock block: codes.getErasureBlocks()){
            codes2.addBlockAt(block);
        }
        codes2.decodeDataObject( result_object );

        //Assertions
        assertEquals(string_data.getValues(), result_object.getValues() );
    }

    /**
     * Tests the Erasure Decoder's response to an attempt at decoding less that the required number of symbols.
     */
    @Test
    public void decodeFailTest(){
        //Encode
        BlackblazeReedSolomonErasureCodes codes = new BlackblazeReedSolomonErasureCodes(appContext);
        codes.encodeDataObject( string_data, n);
        printBlocks(codes.getErasureBlocks());

        List<Integer> removed = new ArrayList<>();
        for (int c = 0; c<t+1; c++){
            removed.add(c);
        }

        BlackblazeReedSolomonErasureCodes codes2 = new BlackblazeReedSolomonErasureCodes(appContext);
        copyAllButSome(codes2, codes.getErasureBlocks(), removed);

        //Assertions
        assertThrows(
                ErasureCodesImpl.IncompleteBlockException.class,
                ()->{ codes2.decodeDataObject(new StringDataObject()); }
        );
    }

    /**
     * Tests the decoding functions in an environment where t codes are removed.
     * The removed codes are randomly selected.
     * @throws Exception
     */
    @Test
    public void randomPopMaxTest() throws Exception{
        //Encode
        BlackblazeReedSolomonErasureCodes codes1 = new BlackblazeReedSolomonErasureCodes(appContext);
        codes1.encodeDataObject( string_data, n);

        ErasureCodesImpl.ErasureBlock[] blocks = codes1.getErasureBlocks();
        //Display resulting Blocks
        System.out.println("Encoded blocks");
        printBlocks(blocks);

        BlackblazeReedSolomonErasureCodes codes2 = new BlackblazeReedSolomonErasureCodes(appContext);
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
        ErasureCodesImpl.ErasureBlock[] blocks2 = codes2.getErasureBlocks();
        printBlocks(blocks2);

        //Decodes
        DataObject<String> result_object = new StringDataObject();
        codes2.decodeDataObject(result_object);

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
        BlackblazeReedSolomonErasureCodes codes1 = new BlackblazeReedSolomonErasureCodes(appContext);
        codes1.encodeDataObject( string_data, n);

        ErasureCodesImpl.ErasureBlock[] blocks = codes1.getErasureBlocks();
        //Display resulting Blocks
        System.out.println("Encoded blocks");
        printBlocks(blocks);

        BlackblazeReedSolomonErasureCodes codes2;
        List<Integer> toRemove;
        DataObject<String> result_object;
        for(int c = 0; c < n; c++){
            //Pick to remove
            toRemove = new ArrayList<>();
            toRemove.add(c); //Picks c
            System.out.printf("Remove code at: %d\n", c);
            //Remove and Decode
            codes2 = new BlackblazeReedSolomonErasureCodes(appContext);
            copyAllButSome(codes2, blocks, toRemove); //Copies all but c
            printBlocks(codes2.getErasureBlocks()); //Shows resulting codes
            result_object = new StringDataObject();
            codes2.decodeDataObject( result_object ); //Decode

            //Assertions
            assertEquals( string_data.getValues(), result_object.getValues() );
        }
    }

    //Auxiliary methods
    private void printBlocks(ErasureCodesImpl.ErasureBlock[] blocks){
        for( ErasureCodesImpl.ErasureBlock row: blocks )
            if (row != null)
                System.out.println( row.position() + ":" + Arrays.toString(row.block_data()) );
    }

    private void copyAllButSome(
            BlackblazeReedSolomonErasureCodes dest,
            ErasureCodesImpl.ErasureBlock[] src,
            List<Integer> some
    ) {
        for( int c = 0; c<n; c++ ){
            if( !some.contains(c) ){
                dest.addBlockAt( src[c] );
            }
        }
    }
}
