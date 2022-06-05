package SystemLayer.Components.DataProcessor;

import SystemLayer.Components.DataProcessor.DataProcessor.ProcessedData;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        simulatedState.getConfigurator().setConfig("ERROR",                 ".1");
        simulatedState.getConfigurator().setConfig("LSH_SEED",              "1234");
        //Erasure codes
        simulatedState.getConfigurator().setConfig("ERASURE_CODES",         "SIMPLE_PARTITION");
        simulatedState.getConfigurator().setConfig("UNIQUE_IDENTIFIER",     "SHA256");

        simulatedState.getConfigurator().setConfig("DATA_PROCESSOR",    "SECRETE_SHARE");
        simulatedState.getConfigurator().setConfig("IV_SEED",           "1234");
        simulatedState.getConfigurator().setConfig("CIPHER_ALGORITHM",  "AES" );
        simulatedState.getConfigurator().setConfig("CIPHER_CONFIG",     "AES/CBC/PKCS5PADDING" );
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
        byte[] rng_data = new byte[256];
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
}