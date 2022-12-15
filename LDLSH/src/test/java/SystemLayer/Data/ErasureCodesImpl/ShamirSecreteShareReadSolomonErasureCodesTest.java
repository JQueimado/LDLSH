package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.DataUnits.ErasureBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ShamirSecreteShareReadSolomonErasureCodesTest {

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
        appContext.getConfigurator().setConfig("ERASURE_CODES", "REED_SOLOMON_SHAMIR");
        appContext.getConfigurator().setConfig("UNIQUE_IDENTIFIER", "SHA256");

        appContext.getConfigurator().setConfig("N_BANDS", "%d".formatted(n));
        appContext.getConfigurator().setConfig("ERASURE_FAULTS", "%d".formatted(t));

        //Data config
        String value = "1=;+A=D=DD?:++222AF+AF9AEE)?9:*11):D):?)889?A#######################################################";
        string_data = (StringDataObject) appContext.getDataObjectFactory().getNewDataObject();
        string_data.setValues(value);
        appContext.getConfigurator().setConfig("VECTOR_SIZE", "%d".formatted(value.length()));

        appContext.getConfigurator().setConfig(
                "VECTOR_SIZE",
                "%d".formatted(string_data.toByteArray().length)
        );
    }

    @Test
    void encodeDecodeComplete() throws Exception {
        //Encode
        ErasureCodes erasureCodes = appContext.getErasureCodesFactory().getNewErasureCodes();
        erasureCodes.encodeDataObject(string_data.toByteArray(), appContext.getNumberOfBands());

        //decode
        ErasureCodes erasureCodes1 = appContext.getErasureCodesFactory().getNewErasureCodes();
        for(ErasureBlock block : erasureCodes.getErasureBlocks()){
            erasureCodes1.addBlockAt(block);
        }
        byte[] data = erasureCodes1.decodeDataObject();
        assertArrayEquals(string_data.toByteArray(), data);

    }

    /**
     * Tests the decoding functions in an environment where t codes are removed.
     * The removed codes are randomly selected.
     * @throws Exception
     */
    @Test
    public void randomPopMaxTest() throws Exception{
        //Encode
        ErasureCodes codes1 = appContext.getErasureCodesFactory().getNewErasureCodes();
        codes1.encodeDataObject( string_data.toByteArray(), n);

        ErasureBlock[] blocks = codes1.getErasureBlocks();

        ErasureCodes codes2 = appContext.getErasureCodesFactory().getNewErasureCodes();
        //Randomly selects t rows to be removed
        Random random = new Random();
        List<Integer> missing_rows = new ArrayList<>();
        for( int c=0; c<t; c++ ){
            missing_rows.add( random.nextInt(n) );
        }

        //Copies some values
        copyAllButSome(codes2, blocks, missing_rows);

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
        ErasureCodes codes1 = appContext.getErasureCodesFactory().getNewErasureCodes();
        codes1.encodeDataObject( string_data.toByteArray(), n);

        ErasureBlock[] blocks = codes1.getErasureBlocks();

        ErasureCodes codes2;
        List<Integer> toRemove;
        DataObject<String> result_object;
        for(int c = 0; c < n; c++){
            //Pick to remove
            toRemove = new ArrayList<>();
            toRemove.add(c); //Picks c

            //Remove and Decode
            codes2 = appContext.getErasureCodesFactory().getNewErasureCodes();
            copyAllButSome(codes2, blocks, toRemove); //Copies all but c

            result_object = new StringDataObject();
            byte[] tempData = codes2.decodeDataObject(); //Decode
            result_object.setByteArray(tempData);

            //Assertions
            assertEquals( string_data.getValues(), result_object.getValues() );
        }
    }

    //Aux
    private void copyAllButSome(
            ErasureCodes dest,
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