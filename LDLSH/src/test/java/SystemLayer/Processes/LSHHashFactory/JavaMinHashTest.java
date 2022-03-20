package SystemLayer.Processes.LSHHashFactory;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JavaMinHashTest {
    DataContainer simulatedState;
    DataObject[] dataObjects;

    @BeforeEach
    void simulateStateAndVectors() throws IOException {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("ACCURACY", "0.1");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");

        //Vectors
        String data_object_type = "STRING";
        DataObjectFactory dataObjectFactory = simulatedState.getDataObjectFactory();
        dataObjects = new DataObject[5];
        dataObjects[0] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[0].setValues("12345");
    }


    @Test
    void getLSH() throws Exception {
        JavaMinHash hashProcessor = new JavaMinHash(simulatedState);

        LSHHash<Integer> hash =  hashProcessor.getLSH(dataObjects[0], simulatedState);

        System.out.println( hash.getValues().toString() );
    }
}