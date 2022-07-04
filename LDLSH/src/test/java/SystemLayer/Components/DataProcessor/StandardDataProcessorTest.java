package SystemLayer.Components.DataProcessor;

import SystemLayer.Components.DataProcessor.DataProcessor.ProcessedData;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.CorruptDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StandardDataProcessorTest {

    private DataContainer simulatedState;

    @BeforeEach
    public void beforeEach(){
        simulatedState = new DataContainer("");
        //Global
        simulatedState.getConfigurator().setConfig("N_BANDS",               "6");
        simulatedState.getConfigurator().setConfig("OBJECT_TYPE",           "STRING");
        //LSH
        simulatedState.getConfigurator().setConfig("LSH_HASH",              "JAVA_MINHASH");
        simulatedState.getConfigurator().setConfig("ERROR",                 ".1");
        simulatedState.getConfigurator().setConfig("LSH_SEED",              "1234");
        //Erasure codes
        simulatedState.getConfigurator().setConfig("ERASURE_CODES",         "SIMPLE_PARTITION");
        simulatedState.getConfigurator().setConfig("UNIQUE_IDENTIFIER",     "SHA256");
        simulatedState.getConfigurator().setConfig("DATA_PROCESSOR",        "STANDARD");
    }

    private DataObject createRandomDataObject(int size){
        simulatedState.getConfigurator().setConfig(DataContainer.dataSize_config, "%d".formatted(size));
        DataObject dataObject = simulatedState.getDataObjectFactory().getNewDataObject();
        Random random = new Random();
        byte[] data = new byte[size];
        random.nextBytes(data);
        dataObject.setByteArray(data);
        return dataObject;
    }

    /**
     * Tests the Standard Data Processor preprocess function by comparing its results to the component generated ones
     * @throws Exception in this context if an exception is thrown the test fails
     */
    @Test
    public void testPreProcess() throws Exception {

        //Object
        String s = "This is a test String";
        DataObject<String> testObject = (StringDataObject) simulatedState.getDataObjectFactory().getNewDataObject();
        testObject.setValues(s);
        simulatedState.getConfigurator().setConfig(DataContainer.dataSize_config, "%d".formatted(s.length()));
        int n_blocks = simulatedState.getNumberOfBands();

        //LSH
        LSHHash testLSH = simulatedState.getLshHashFactory().getNewLSHHash();
        testLSH.setObject(testObject.toByteArray(), n_blocks);

        //Uid
        UniqueIdentifier testUID = simulatedState.getUniqueIdentifierFactory().getNewUniqueIdentifier();
        testUID.setObject(testObject.toByteArray());

        //Erasure codes
        ErasureCodes testErasureCodes = simulatedState.getErasureCodesFactory().getNewErasureCodes();
        testErasureCodes.encodeDataObject(testObject.toByteArray(), n_blocks);

        //PreProcess
        ProcessedData processedData = simulatedState.getDataProcessor().preProcessData(testObject);

        //Assertions
        assertEquals( testLSH, processedData.object_lsh() );
        assertEquals( testUID, processedData.object_uid() );
        assertEquals( testErasureCodes, processedData.object_erasureCodes() );
    }

    /**
     * Tests the Standard Data Processor postprocessor function by comparing its results to the component generated ones
     * @throws Exception in this context if an exception is thrown the test fails
     */
    @Test
    public void testPostprocessor() throws Exception {

        //Object
        String s = "This is a test String";
        DataObject<String> testObject = (StringDataObject) simulatedState.getDataObjectFactory().getNewDataObject();
        testObject.setValues(s);
        int n_blocks = simulatedState.getNumberOfBands();

        simulatedState.getConfigurator().setConfig(DataContainer.dataSize_config, "%d".formatted(s.length()));

        //Uid
        UniqueIdentifier testUID = simulatedState.getUniqueIdentifierFactory().getNewUniqueIdentifier();
        testUID.setObject(testObject.toByteArray());

        //Erasure codes
        ErasureCodes testErasureCodes = simulatedState.getErasureCodesFactory().getNewErasureCodes();
        testErasureCodes.encodeDataObject(testObject.toByteArray(), n_blocks);

        //PostProcess
        DataObject<String> resultObject = (StringDataObject) simulatedState.getDataProcessor()
                .postProcess( testErasureCodes, testUID );

        //Assertions
        assertEquals(testObject, resultObject);
    }

    /**
     * Tests the Standard Data Processor postprocessor and preprocessor functions by preprocessing a random string of
     * bytes and postprocessing its result
     * @throws Exception in this context if an exception is thrown the test fails
     */
    @Test
    public void testRandomPreAndPostprocessor() throws Exception {
        //Object
        DataObject<String> testObject = (StringDataObject) createRandomDataObject(256);

        //PreProcess
        ProcessedData processedData = simulatedState.getDataProcessor().preProcessData(testObject);

        //PostProcess
        DataObject<String> resultObject = (StringDataObject) simulatedState.getDataProcessor()
                .postProcess( processedData.object_erasureCodes(), processedData.object_uid() );

        //Assertions
        assertEquals(testObject, resultObject);
    }

    /**
     * Tests the StandardDataProcessors validation technique by swapping a random block by a random array of bytes
     * @throws Exception if an exception is thrown the tet fails
     */
    @Test
    public void test_validation() throws Exception {
        //PreProcess
        DataObject<String> testObject = (StringDataObject) createRandomDataObject(500);
        ProcessedData processedData = simulatedState.getDataProcessor().preProcessData(testObject);
        ErasureCodes erasureCodes = processedData.object_erasureCodes();
        UniqueIdentifier uniqueIdentifier = processedData.object_uid();

        assertDoesNotThrow( ()->{
            simulatedState.getDataProcessor().postProcess(erasureCodes, uniqueIdentifier);
        } );

        //Alter code
        Random random = new Random();
        int corrupt_block_position = random.nextInt( simulatedState.getNumberOfBands() );
        byte[] corrupt_block_data = new byte[ erasureCodes.getBlockAt(0).block_data().length ];
        random.nextBytes(corrupt_block_data);
        erasureCodes.addBlockAt( new ErasureCodesImpl.ErasureBlock( corrupt_block_data, corrupt_block_position ) );

        assertThrows( CorruptDataException.class, ()->{
            simulatedState.getDataProcessor().postProcess(erasureCodes, uniqueIdentifier);
        } );

    }

}