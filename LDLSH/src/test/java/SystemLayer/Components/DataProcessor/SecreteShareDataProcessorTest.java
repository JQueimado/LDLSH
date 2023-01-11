package SystemLayer.Components.DataProcessor;

import SystemLayer.Components.DataProcessor.DataProcessor.ProcessedData;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.DataUnits.ErasureBlock;
import SystemLayer.Data.DataUnits.ErasureBlockImpl;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.CorruptDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SecreteShareDataProcessorTest {

    private DataContainer simulatedState;

    @BeforeEach
    public void beforeEach(){
        simulatedState = new DataContainer("");
        //Global
        simulatedState.getConfigurator().setConfig("N_BANDS",               "6");
        simulatedState.getConfigurator().setConfig("OBJECT_TYPE",           "STRING");
        //LSH
        simulatedState.getConfigurator().setConfig("LSH_HASH",              "JAVA_MINHASH");
        simulatedState.getConfigurator().setConfig("THRESHOLD",             ".9");
        simulatedState.getConfigurator().setConfig("LSH_SEED",              "1234");
        //Erasure codes
        simulatedState.getConfigurator().setConfig("ERASURE_CODES",         "REED_SOLOMON");
        simulatedState.getConfigurator().setConfig("ERASURE_FAULTS",       "2" );
        simulatedState.getConfigurator().setConfig("UNIQUE_IDENTIFIER",     "SHA256");

        simulatedState.getConfigurator().setConfig("DATA_PROCESSOR",    "SECRETE_SHARE");
        simulatedState.getConfigurator().setConfig("IV_SEED",           "abcdefghijklmnop");
        simulatedState.getConfigurator().setConfig("CIPHER_ALGORITHM",  "AES" );
        simulatedState.getConfigurator().setConfig("CIPHER_CONFIG",     "AES/CBC/PKCS5PADDING" );
    }

    private DataObject<?> createRandomDataObject(int size){
        simulatedState.getConfigurator().setConfig(DataContainer.dataSize_config, "%d".formatted(size));
        DataObject<?> dataObject = simulatedState.getDataObjectFactory().getNewDataObject();
        Random random = new Random();
        byte[] data = new byte[size];
        random.nextBytes(data);
        dataObject.setByteArray(data);
        return dataObject;
    }

    /**
     * Tests the Secrete Share Data Processor postprocessor and preprocessor functions by preprocessing a random string of
     * bytes and postprocessing its result
     * @throws Exception in this context if an exception is thrown the test fails
     */
    @Test
    public void SecreteShareDataProcessor_testRandomPreAndPostprocessor() throws Exception {
        simulatedState.getConfigurator().setConfig(
                DataContainer.dataSize_config,
                "256"
        );

        //Object
        Random random = new Random();
        byte[] rng_data = new byte[100];
        random.nextBytes(rng_data);
        DataObject<String> testObject = (StringDataObject) simulatedState.getDataObjectFactory().getNewDataObject();
        testObject.setByteArray(rng_data);

        //PreProcess
        ProcessedData processedData = simulatedState.getDataProcessor().preProcessData(testObject);

        //PostProcess
        DataObject<String> resultObject = (StringDataObject) simulatedState.getDataProcessor()
                .postProcess( processedData.object_erasureCodes(), processedData.object_uid() );

        //Assertions
        assertEquals(testObject, resultObject);
    }

    @Test
    public void test_validation_failed() throws Exception {
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
        int corrupt_block_position = random.nextInt( simulatedState.getNumberOfBands() - 2 );
        byte[] corrupt_block_data = new byte[ erasureCodes.getBlockAt(0).getBlock().length ];
        random.nextBytes(corrupt_block_data);
        erasureCodes.addBlockAt( new ErasureBlockImpl( corrupt_block_data, corrupt_block_position ) );

        assertThrows( CorruptDataException.class, ()->{
            simulatedState.getDataProcessor().postProcess(erasureCodes, uniqueIdentifier);
        } );

    }

    @Test
    public void test_validation_not_failed() throws Exception {
        //PreProcess
        DataObject<String> testObject = (StringDataObject) createRandomDataObject(500);
        ProcessedData processedData = simulatedState.getDataProcessor().preProcessData(testObject);
        ErasureCodes erasureCodes = processedData.object_erasureCodes();
        UniqueIdentifier uniqueIdentifier = processedData.object_uid();

        assertDoesNotThrow( ()->{
            simulatedState.getDataProcessor().postProcess(erasureCodes, uniqueIdentifier);
        } );

    }
}