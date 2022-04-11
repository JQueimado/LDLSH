package SystemLayer.Data.LSHHashImpl;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import info.debatty.java.lsh.MinHash;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static SystemLayer.Data.LSHHashImpl.LSHHash.*;
import static org.junit.jupiter.api.Assertions.*;

class JavaMinHashTest {

    DataContainer simulatedState;
    StringDataObject[] dataObjects;

    @BeforeEach
    void simulateStateAndVectors() throws IOException {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("ERROR", "0.1");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "12345");

        //Vectors
        dataObjects = new StringDataObject[4];

        dataObjects[0] = new StringDataObject();
        dataObjects[0].setValues("12345");

        //Jaccard Distance to 0: 0.2
        dataObjects[1] = new StringDataObject();
        dataObjects[1].setValues("12355");

        //Jaccard Distance to 0: 0.0
        dataObjects[2] = new StringDataObject();
        dataObjects[2].setValues("12345");

        //Jaccard Distance to 0: 0.8
        dataObjects[3] = new StringDataObject();
        dataObjects[3].setValues("55555");
    }

    @Test
    void setObjectGetSignature() {
        JavaMinHash hash1 = new JavaMinHash(dataObjects[0], 2, simulatedState);
        JavaMinHash hash2 = new JavaMinHash(dataObjects[2], 2, simulatedState);
        assertArrayEquals(hash1.getSignature(), hash2.getSignature());
        hash1.setObject(dataObjects[1], 2);
        MatcherAssert.assertThat(hash1.getSignature(), IsNot.not( IsEqual.equalTo( hash2.getSignature() ) ) );
    }

    @Test
    void getBlocks() {
        int n_blocks = 6;
        JavaMinHash hash = new JavaMinHash( dataObjects[0], n_blocks, simulatedState );
        LSHHashBlock[] blocks = hash.getBlocks();
        assertNotNull(blocks);
        assertEquals(blocks.length, n_blocks);

        byte[] signature = hash.getSignature();
        String signature_String = new String(signature, StandardCharsets.UTF_8);

        StringBuilder rebuilt_signature = new StringBuilder();
        for ( LSHHashBlock block : blocks ){
            rebuilt_signature.append(new String(block.lshBlock(), StandardCharsets.UTF_8));
        }

        assertEquals(signature_String, rebuilt_signature.toString());
    }

    @Test
    void getMinHash() throws Exception {
        MinHash minHash = JavaMinHash.getMinHash(simulatedState);
        assertNotNull(minHash);
    }

    @Test
    void getBlockAt_SingleBlock() {
        int n_blocks = 1;
        int position = 0;
        LSHHash hash = new JavaMinHash( dataObjects[0], n_blocks, simulatedState );
        LSHHashBlock[] blocks = hash.getBlocks();
        LSHHashBlock block = hash.getBlockAt(position);
        assertArrayEquals(blocks[position].lshBlock(), block.lshBlock());
    }

    @Test
    void getBlockAt_AllBlocks(){
        int n_blocks = 6;
        JavaMinHash hash = new JavaMinHash( dataObjects[0], n_blocks, simulatedState );
        LSHHashBlock[] blocks = hash.getBlocks();
        for (int i = 0; i<n_blocks; i++) {
            LSHHashBlock block = hash.getBlockAt(i);
            assertArrayEquals(blocks[i].lshBlock(), block.lshBlock());
        }
    }
}