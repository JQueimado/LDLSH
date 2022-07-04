package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimplePartitionErasureCodesTest {

    private DataContainer simulatedState;

    @BeforeEach
    public void beforeEach(){
        simulatedState = new DataContainer("");
        simulatedState.getConfigurator().setConfig("ERASURE_CODES", "SIMPLE_PARTITION");
        simulatedState.getConfigurator().setConfig("N_BANDS", "4");
        simulatedState.getConfigurator().setConfig("OBJECT_TYPE", "BYTE_ARRAY");
    }

    @Test
    void encodeDataObject() {
        byte[] test_data = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};

        ErasureBlock[] expected_data = new ErasureBlock[]{
                new ErasureBlock( new byte[]{1,5,9,13}, 0 ),
                new ErasureBlock( new byte[]{2,6,10,14}, 1 ),
                new ErasureBlock( new byte[]{3,7,11,15}, 2 ),
                new ErasureBlock( new byte[]{4,8,12,16}, 3 )
        };

        simulatedState.getConfigurator().setConfig("VECTOR_SIZE", "%d".formatted(test_data.length));

        ErasureCodes erasureCodes = simulatedState.getErasureCodesFactory().getNewErasureCodes();
        erasureCodes.encodeDataObject(test_data, simulatedState.getNumberOfBands());

        //Assertions
        for(int i=0; i<expected_data.length; i++){
            assertEquals( expected_data[i], erasureCodes.getErasureBlocks()[i] );
        }
    }

    @Test
    void encodeDataObjectPaddingTest() {
        byte[] test_data = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14};

        ErasureBlock[] expected_data = new ErasureBlock[]{
                new ErasureBlock( new byte[]{1,5,9,13}, 0 ),
                new ErasureBlock( new byte[]{2,6,10,14}, 1 ),
                new ErasureBlock( new byte[]{3,7,11,0}, 2 ),
                new ErasureBlock( new byte[]{4,8,12,0}, 3 )
        };

        simulatedState.getConfigurator().setConfig("VECTOR_SIZE", "%d".formatted(test_data.length));

        ErasureCodes erasureCodes = simulatedState.getErasureCodesFactory().getNewErasureCodes();
        erasureCodes.encodeDataObject(test_data, simulatedState.getNumberOfBands());

        //Assertions
        for(int i=0; i<expected_data.length; i++){
            assertEquals( expected_data[i], erasureCodes.getErasureBlocks()[i] );
        }
    }

    @Test
    void decodeDataObject() throws Exception {
        ErasureBlock[] test_data = new ErasureBlock[]{
                new ErasureBlock( new byte[]{1,5,9,13}, 0 ),
                new ErasureBlock( new byte[]{2,6,10,14}, 1 ),
                new ErasureBlock( new byte[]{3,7,11,15}, 2 ),
                new ErasureBlock( new byte[]{4,8,12,16}, 3 )
        };

        byte[] expected_data = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        simulatedState.getConfigurator().setConfig(
                "VECTOR_SIZE",
                "%d".formatted(expected_data.length)
        );
        simulatedState.setErasureCodesDataSize(simulatedState.getObjectByteSize());

        ErasureCodes erasureCodes = simulatedState.getErasureCodesFactory().getNewErasureCodes();
        for ( ErasureBlock block : test_data ){
            erasureCodes.addBlockAt(block);
        }
        byte[] resultData = erasureCodes.decodeDataObject();

        //Assertions
        assertArrayEquals( expected_data, resultData );
    }

    @Test
    void decodeDataObjectPaddingTest() throws Exception {
        ErasureBlock[] test_data = new ErasureBlock[]{
                new ErasureBlock( new byte[]{1,5,9,13}, 0 ),
                new ErasureBlock( new byte[]{2,6,10,0}, 1 ),
                new ErasureBlock( new byte[]{3,7,11,0}, 2 ),
                new ErasureBlock( new byte[]{4,8,12,0}, 3 )
        };

        byte[] expected_data = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13};
        simulatedState.getConfigurator().setConfig(
                "VECTOR_SIZE",
                "%d".formatted(expected_data.length)
        );
        simulatedState.setErasureCodesDataSize(simulatedState.getObjectByteSize());

        ErasureCodes erasureCodes = simulatedState.getErasureCodesFactory().getNewErasureCodes();
        for ( ErasureBlock block : test_data ){
            erasureCodes.addBlockAt(block);
        }
        byte[] resultData = erasureCodes.decodeDataObject();

        //Assertions
        assertArrayEquals( expected_data, resultData );
    }
}