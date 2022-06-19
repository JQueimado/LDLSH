package SystemLayer.Data.LSHTests;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHashImpl;
import info.debatty.java.lsh.MinHash;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class JavaMinHashSimilarityTests {

    DataContainer simulatedState;
    DataObject<String>[] dataObjects;

    @BeforeEach
    void simulateStateAndVectors() throws Exception {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("ERROR", "0.1");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "11111");
        configurator.setConfig("LSH_HASH", "JAVA_MINHASH");

        //Vectors
        String data_object_type = "STRING";
        DataObjectFactory dataObjectFactory = simulatedState.getDataObjectFactory();
        dataObjects = new StringDataObject[5];

        dataObjects[0] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[0].setValues("12345");

        //Jaccard Distance to 0: 0.2
        dataObjects[1] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[1].setValues("12355");

        //Jaccard Distance to 0: 0.0
        dataObjects[2] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[2].setValues("12345");

        //Jaccard Distance to 0: 0.8
        dataObjects[3] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[3].setValues("55555");

        dataObjects[4] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[4].setValues("54321");
    }

    /**
     * Evaluates the similarity between 2 data objects against an expected similarity
     * This test only displays the values since LSH is a probabilistic algorithm and results are not accurate.
     * @param dataObject1 data object 1
     * @param dataObject2 data object 2
     * @param expected_similarity hand calculated similarity
     */
    void similarityTest(DataObject<String> dataObject1, DataObject<String> dataObject2, double expected_similarity ){
        JavaMinHash hash = new JavaMinHash(dataObject1, 2, simulatedState);
        byte[] signature = hash.getSignature();
        int[] ints = toIntArray(signature);

        JavaMinHash hash2 = new JavaMinHash(dataObject2, 2, simulatedState);
        byte[] signature2 = hash2.getSignature();
        int[] ints2 = toIntArray(signature2);

        double error = Double.parseDouble( simulatedState.getConfigurator().getConfig("ERROR") );
        long seed = Long.parseLong( simulatedState.getConfigurator().getConfig("LSH_SEED") );

        MinHash minHash = new MinHash(error, dataObject1.objectByteSize(5),seed);
        double similarity = minHash.similarity(ints, ints2);
        double jaccard_similarity = MinHash.jaccardIndex(
                toIntSet( dataObject1.toByteArray() ),
                toIntSet( dataObject2.toByteArray() )
        );
        System.out.printf(
                """
                Similarity test between "%s" and "%s":
                \t-Expected Similarity: %f
                \t-Actual Similarity: %f
                \t-Jaccard Similarity: %f
                """,
                dataObject1.getValues(),
                dataObject2.getValues(),
                expected_similarity,
                similarity,
                jaccard_similarity
        );
    }

    //Similarity tests
    @Test
    void getValuesSimilarity_01() {
        similarityTest(dataObjects[0], dataObjects[1], 0.8 );
    }

    @Test
    void getValuesSimilarity_02() {
        similarityTest(dataObjects[0], dataObjects[2], 1);
    }

    @Test
    void getValuesSimilarity_03() {
        similarityTest(dataObjects[0], dataObjects[3], 0.2);
    }

    @Test
    void getValuesSimilarity_04() {
        similarityTest(dataObjects[0], dataObjects[4], 0.2);
    }

    //Auxiliary methods

    /**
     * transforms an array of bytes into an array of ints byte by byte
     * @param array input array
     * @return resulting array
     */
    private int[] toIntArray( byte[] array ){
        int[] values = new int[array.length];
        int i;
        int j = array.length;
        for (i = 0; i<j; i++ ){
            int value = array[i];
            values[i] = value;
        }
        return values;
    }

    /**
     * Transforms an array of bytes into an set of Integers byte by byte;
     * @param array input array
     * @return resulting set
     */
    private Set<Integer> toIntSet(byte[] array ){
        Set<Integer> set = new HashSet<>();
        int i;
        int j = array.length;
        for (i = 0; i<j; i++ ){
            int value = array[i];
            set.add( value );
        }
        return set;
    }

    /* Component Tests */

    /**
     * Tests the set object and get signature functions by encoding its components and return the result value
     */
    @Test
    void setObjectGetSignature() {
        LSHHash hash1 = simulatedState.getLshHashFactory().getNewLSHHash();
        hash1.setObject(dataObjects[0].toByteArray(), 2);

        LSHHash hash2 = simulatedState.getLshHashFactory().getNewLSHHash();
        hash2.setObject(dataObjects[2].toByteArray(), 2);

        assertArrayEquals(hash1.getSignature(), hash2.getSignature());
        hash1.setObject(dataObjects[1].toByteArray(), 2);
        MatcherAssert.assertThat(hash1.getSignature(), IsNot.not( IsEqual.equalTo( hash2.getSignature() ) ) );
    }

    @Test
    void getBlocks(){
        int n_blocks = 6;
        LSHHash hash = simulatedState.getLshHashFactory().getNewLSHHash();
        hash.setObject(dataObjects[0].toByteArray(), n_blocks);
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
    void getBlockAt_SingleBlock(){
        int n_blocks = 1;
        int position = 0;
        LSHHash hash = simulatedState.getLshHashFactory().getNewLSHHash();
        hash.setObject(dataObjects[0].toByteArray(), n_blocks);
        LSHHashImpl.LSHHashBlock[] blocks = hash.getBlocks();
        LSHHashImpl.LSHHashBlock block = hash.getBlockAt(position);
        assertArrayEquals(blocks[position].lshBlock(), block.lshBlock());
    }

    @Test
    void getBlockAt_AllBlocks(){
        int n_blocks = 6;
        LSHHash hash = simulatedState.getLshHashFactory().getNewLSHHash();
        hash.setObject(dataObjects[0].toByteArray(), n_blocks);
        LSHHashImpl.LSHHashBlock[] blocks = hash.getBlocks();
        for (int i = 0; i<n_blocks; i++) {
            LSHHashImpl.LSHHashBlock block = hash.getBlockAt(i);
            assertArrayEquals(blocks[i].lshBlock(), block.lshBlock());
        }
    }
}
