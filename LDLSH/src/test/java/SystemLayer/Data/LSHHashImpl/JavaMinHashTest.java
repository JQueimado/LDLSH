package SystemLayer.Data.LSHHashImpl;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import info.debatty.java.lsh.MinHash;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static SystemLayer.Data.LSHHashImpl.LSHHash.*;
import static org.junit.jupiter.api.Assertions.*;

class JavaMinHashTest {

    DataContainer simulatedState;
    DataObject[] dataObjects;

    @BeforeEach
    void simulateStateAndVectors() throws IOException {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("ERROR", "0.1");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "11111");

        //Vectors
        String data_object_type = "STRING";
        DataObjectFactory dataObjectFactory = simulatedState.getDataObjectFactory();
        dataObjects = new DataObject[4];

        dataObjects[0] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[0].setValues("12345");

        //Jaccard Distance to 0: 0.2
        dataObjects[1] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[1].setValues("12355");

        //Jaccard Distance to 0: 0.0
        dataObjects[2] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[2].setValues("12345");

        //Jaccard Distance to 0: 0.8
        dataObjects[3] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[3].setValues("55555");
    }

    @Test
    void setObjectGetSignature() throws Exception {
        JavaMinHash hash1 = new JavaMinHash(dataObjects[0], 2, simulatedState);
        JavaMinHash hash2 = new JavaMinHash(dataObjects[2], 2, simulatedState);
        assertArrayEquals(hash1.getSignature(), hash2.getSignature());
        hash1.setObject(dataObjects[1], 2);
        MatcherAssert.assertThat(hash1.getSignature(), IsNot.not( IsEqual.equalTo( hash2.getSignature() ) ) );
    }

    @Test
    void getBlocks() {
        JavaMinHash hash = new JavaMinHash( dataObjects[0], 3, simulatedState );
        LSHHashBlock[] blocks = hash.getBlocks();
        assertNotNull(blocks);
        assertEquals(blocks.length, 3);
    }

    @Test
    void getMinHash() throws Exception {
        assertThrows(Exception.class, JavaMinHash::getMinHash);
        //Initiates the Minhash
        MinHash minHash = JavaMinHash.getMinHash(simulatedState);
        assertNotNull(minHash);
    }

    @Test
    void getBlockAt() {
    }
}