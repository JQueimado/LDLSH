package SystemLayer.Components.DataProcessor;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataProcessorTest {

    private DataContainer simulatedState;

    @BeforeEach
    public void beforeEach(){
        simulatedState = new DataContainer("");
        //Global
        simulatedState.getConfigurator().setConfig("N_BANDS",               "2");
        simulatedState.getConfigurator().setConfig("OBJECT_TYPE",           "STRING");
        //LSH
        simulatedState.getConfigurator().setConfig("LSH_HASH",              "JAVA_MINHASH");
        simulatedState.getConfigurator().setConfig("ERROR",                 ".1");
        simulatedState.getConfigurator().setConfig("LSH_SEED",              "1234");
        //Erasure codes
        simulatedState.getConfigurator().setConfig("ERASURE_CODES",         "SIMPLE_PARTITION");
        simulatedState.getConfigurator().setConfig("UNIQUE_IDENTIFIER",     "SHA256");
    }

    //StandardDataProcessor
    private void config_Standard(){
        simulatedState.getConfigurator().setConfig("DATA_PROCESSOR", "STANDARD");
    }

    /**
     * Tests the Standard Data Processor preprocess function by comparing its results to the component generated ones
     * @throws Exception in this context if an exception is thrown the test fails
     */
    @Test
    public void StandardDataProcessor_testPreProcess() throws Exception {
        config_Standard();

        //Object
        DataObject<String> testObject = (StringDataObject) simulatedState.getDataObjectFactory().getNewDataObject();
        testObject.setValues("This is a test String");
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
        DataProcessor.ProcessedData processedData = simulatedState.getDataProcessor().preProcessData(testObject);

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
    public void StandardDataProcessor_testPostprocessor() throws Exception {
       config_Standard();

        //Object
        DataObject<String> testObject = (StringDataObject) simulatedState.getDataObjectFactory().getNewDataObject();
        testObject.setValues("This is a test String");
        int n_blocks = simulatedState.getNumberOfBands();

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
    public void StandardDataProcessor_testRandomPreAndPostprocessor() throws Exception {
        config_Standard();

        //Object
        Random random = new Random();
        byte[] rng_data = new byte[256];
        random.nextBytes(rng_data);
        DataObject<String> testObject = (StringDataObject) simulatedState.getDataObjectFactory().getNewDataObject();
        testObject.setByteArray(rng_data);

        //PreProcess
        DataProcessor.ProcessedData processedData = simulatedState.getDataProcessor().preProcessData(testObject);

        //PostProcess
        DataObject<String> resultObject = (StringDataObject) simulatedState.getDataProcessor()
                .postProcess( processedData.object_erasureCodes(), processedData.object_uid() );

        //Assertions
        assertEquals(testObject, resultObject);
    }

    //SecreteShare using AES-CBC
    private void config_SecreteShareWithAESCBC(){
        simulatedState.getConfigurator().setConfig("DATA_PROCESSOR",    "SECRETE_SHARE");
        simulatedState.getConfigurator().setConfig("IV_SEED",           "1234");
        simulatedState.getConfigurator().setConfig("CIPHER_ALGORITHM",  "AES" );
        simulatedState.getConfigurator().setConfig("CIPHER_CONFIG",     "AES/CBC/PKCS5PADDING" );
    }
    /**
     * Tests the Standard Data Processor preprocess function by comparing its results to the component generated ones
     * @throws Exception in this context if an exception is thrown the test fails
     */
    //@Test
    public void SecreteShareDataProcessor_testPreProcess() throws Exception {
        config_SecreteShareWithAESCBC();

        //Object
        DataObject<String> testObject = (StringDataObject) simulatedState.getDataObjectFactory().getNewDataObject();
        testObject.setValues("This is a test String");
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
        DataProcessor.ProcessedData processedData = simulatedState.getDataProcessor().preProcessData(testObject);

        //Assertions
        assertEquals( testLSH, processedData.object_lsh() );
        assertEquals( testUID, processedData.object_uid() );
        assertEquals( testErasureCodes, processedData.object_erasureCodes() );
    }

    /**
     * Tests the Secrete Share Data Processor postprocessor function by comparing its results to the component generated ones
     * @throws Exception in this context if an exception is thrown the test fails
     */
    //@Test
    public void SecreteShareDataProcessor_testPostprocessor() throws Exception {
        config_SecreteShareWithAESCBC();

        //Object
        DataObject<String> testObject = (StringDataObject) simulatedState.getDataObjectFactory().getNewDataObject();
        testObject.setValues("This is a test String");
        int n_blocks = simulatedState.getNumberOfBands();

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
     * Tests the Secrete Share Data Processor postprocessor and preprocessor functions by preprocessing a random string of
     * bytes and postprocessing its result
     * @throws Exception in this context if an exception is thrown the test fails
     */
    @Test
    public void SecreteShareDataProcessor_testRandomPreAndPostprocessor() throws Exception {
        config_SecreteShareWithAESCBC();

        //Object
        Random random = new Random();
        byte[] rng_data = new byte[256];
        random.nextBytes(rng_data);
        DataObject<String> testObject = (StringDataObject) simulatedState.getDataObjectFactory().getNewDataObject();
        testObject.setByteArray(rng_data);

        //PreProcess
        DataProcessor.ProcessedData processedData = simulatedState.getDataProcessor().preProcessData(testObject);

        //PostProcess
        DataObject<String> resultObject = (StringDataObject) simulatedState.getDataProcessor()
                .postProcess( processedData.object_erasureCodes(), processedData.object_uid() );

        //Assertions
        assertEquals(testObject, resultObject);
    }
}