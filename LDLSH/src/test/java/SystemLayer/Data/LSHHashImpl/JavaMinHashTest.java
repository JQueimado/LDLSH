package SystemLayer.Data.LSHHashImpl;

import Factories.DataFactories.LSHHashFactory;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import info.debatty.java.lsh.MinHash;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JavaMinHashTest {

    DataContainer simulatedState;
    StringDataObject[] dataObjects;
    LSHHashFactory lshHashFactory;

    @BeforeEach
    void simulateStateAndVectors() throws IOException {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("ERROR", "0.1");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "12345");
        lshHashFactory = simulatedState.getLshHashFactory();

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
    void setObjectGetSignature() throws Exception {
        LSHHash hash1 = lshHashFactory.getNewLSHHash( "JAVA_MINHASH", simulatedState );
        hash1.setObject(dataObjects[0], 2);

        LSHHash hash2 = lshHashFactory.getNewLSHHash( "JAVA_MINHASH", simulatedState );
        hash2.setObject(dataObjects[2], 2);

        assertArrayEquals(hash1.getSignature(), hash2.getSignature());
        hash1.setObject(dataObjects[1], 2);
        MatcherAssert.assertThat(hash1.getSignature(), IsNot.not( IsEqual.equalTo( hash2.getSignature() ) ) );
    }

    @Test
    void getBlocks() throws Exception {
        int n_blocks = 6;
        LSHHash hash = lshHashFactory.getNewLSHHash( "JAVA_MINHASH", simulatedState );
        hash.setObject(dataObjects[0], n_blocks);
        LSHHashImpl.LSHHashBlock[] blocks = hash.getBlocks();
        assertNotNull(blocks);
        assertEquals(blocks.length, n_blocks);

        byte[] signature = hash.getSignature();
        String signature_String = new String(signature, StandardCharsets.UTF_8);

        StringBuilder rebuilt_signature = new StringBuilder();
        for ( LSHHashImpl.LSHHashBlock block : blocks ){
            rebuilt_signature.append(new String(block.lshBlock(), StandardCharsets.UTF_8));
        }

        assertEquals(signature_String, rebuilt_signature.toString());
    }

    @Test
    void getMinHash() throws Exception {
        MinHash minHash = JavaMinHash.getMinHash();
        assertNotNull(minHash);
    }

    @Test
    void getBlockAt_SingleBlock() throws Exception {
        int n_blocks = 1;
        int position = 0;
        LSHHash hash = lshHashFactory.getNewLSHHash( "JAVA_MINHASH", simulatedState );
        hash.setObject(dataObjects[0], n_blocks);
        LSHHashImpl.LSHHashBlock[] blocks = hash.getBlocks();
        LSHHashImpl.LSHHashBlock block = hash.getBlockAt(position);
        assertArrayEquals(blocks[position].lshBlock(), block.lshBlock());
    }

    @Test
    void getBlockAt_AllBlocks() throws Exception{
        int n_blocks = 6;
        LSHHash hash = lshHashFactory.getNewLSHHash( "JAVA_MINHASH", simulatedState );
        hash.setObject(dataObjects[0], n_blocks);
        LSHHashImpl.LSHHashBlock[] blocks = hash.getBlocks();
        for (int i = 0; i<n_blocks; i++) {
            LSHHashImpl.LSHHashBlock block = hash.getBlockAt(i);
            assertArrayEquals(blocks[i].lshBlock(), block.lshBlock());
        }
    }
}