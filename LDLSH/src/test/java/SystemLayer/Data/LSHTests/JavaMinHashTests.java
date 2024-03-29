package SystemLayer.Data.LSHTests;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Components.AdditionalStructures.AuxiliarImplementations.NgramProcessor;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.DataUnits.LSHHashBlock;
import info.debatty.java.lsh.MinHash;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class JavaMinHashTests {

    DataContainer simulatedState;
    DataObject<String>[] dataObjects;

    @BeforeEach
    void simulateStateAndVectors() throws Exception {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("N_BANDS", "3");
        configurator.setConfig("THRESHOLD", "0.95");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "11111");
        configurator.setConfig("LSH_HASH", "JAVA_MINHASH_NGRAMS");
        configurator.setConfig("NGRAMS_LEVEL", "3");

        //Vectors
        String data_object_type = "STRING";
        DataObjectFactory dataObjectFactory = simulatedState.getDataObjectFactory();
        dataObjects = new StringDataObject[4];

        dataObjects[0] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[0].setValues("==1ADDD?2,2+2,AEDEAD>:ACFE+A?AACFEEC@9E3C?*CDD9B9?D=<**0??4D?B@;))=.==@C@CA)=.)=;)7?@AA@@2.>@;;>A;>>");

        //Jaccard Distance to 0: 0.2
        dataObjects[1] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[1].setValues("+=1ADDD;:222A:EE93CEEF3++3<9CCC<EC>EDFCC?C*BBDD>@DEIDBDD<?B/8/8=B@##################################");

        //Jaccard Distance to 0: 0.0
        dataObjects[2] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[2].setValues("1=;+A=D=DD?:++222AF+AF9AEE)?9:*11):D):?)889?A#######################################################");

        //Jaccard Distance to 0: 0.8
        dataObjects[3] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[3].setValues("1::ADD@D?)+222AEE?C22:):C?D?):*0:*???BD899B;AAAADCAA=C;A)=@37?)7?###################################");

        //dataObjects[4] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        //dataObjects[4].setValues("54321");
    }

    /**
     * Evaluates the similarity between 2 data objects against an expected similarity
     * This test only displays the values since LSH is a probabilistic algorithm and results are not accurate.
     * @param o1 data object 1
     * @param o2 data object 2
     */
    private void similarityTest_template(DataObject<String> o1, DataObject<String> o2, double expected ){
        LSHHash hash1 = simulatedState.getLshHashFactory().getNewLSHHash();
        hash1.setObject(o1.toByteArray(), 2);
        byte[] signature = hash1.getSignature();
        int[] ints = toIntArray(signature);

        LSHHash hash2 = simulatedState.getLshHashFactory().getNewLSHHash();
        hash2.setObject(o2.toByteArray(), 2);
        byte[] signature2 = hash2.getSignature();
        int[] ints2 = toIntArray(signature2);

        long seed = Long.parseLong( simulatedState.getConfigurator().getConfig("LSH_SEED") );
        MinHash minHash = new MinHash(signature.length/4, o1.objectByteSize(100),seed);
        double hash_distance = 1 - minHash.similarity(ints, ints2);

        assertEquals( expected, hash_distance, minHash.error() );
    }

    //Similarity tests
    @Test
    void similarityTest_1() {
        similarityTest_template(
                dataObjects[0],
                dataObjects[0],
                0
        );
    }

    @Test
    void similarityTest_2(){
        similarityTest_template(
                dataObjects[0],
                dataObjects[1],
                0.9745222929936306
        );
    }

    @Test
    void similarityTest_3(){
        similarityTest_template(
                dataObjects[0],
                dataObjects[2],
                0.9928571428571429
        );
    }

    @Test
    void similarityTest_4(){
        similarityTest_template(
                dataObjects[0],
                dataObjects[3],
                0.9807692307692307
        );
    }

    @Test
    void similarityTest_5(){
        similarityTest_template(
                dataObjects[1],
                dataObjects[2],
                0.9724770642201834
        );
    }

    @Test
    void similarityTest_6(){
        similarityTest_template(
                dataObjects[1],
                dataObjects[3],
                0.9682539682539683
        );
    }

    @Test
    void similarityTest_7(){
        similarityTest_template(
                dataObjects[2],
                dataObjects[3],
                0.9523809523809523
        );
    }

    //Auxiliary methods
    /**
     * transforms an array of bytes into an array of ints byte by byte
     * @param array input array
     * @return resulting array
     */
    private int[] toIntArray( byte[] array ){
        ByteBuffer byteBuffer = ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        int[] intArray = new int[intBuffer.remaining()];
        intBuffer.get(intArray);
        return intArray;
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
        hash2.setObject(dataObjects[0].toByteArray(), 2);

        assertArrayEquals(hash1.getSignature(), hash2.getSignature());
        hash1.setObject(dataObjects[1].toByteArray(), 2);
        MatcherAssert.assertThat(hash1.getSignature(), IsNot.not( IsEqual.equalTo( hash2.getSignature() ) ) );
    }

    @Test
    void getBlocks(){
        int n_blocks = 6;
        LSHHash hash = simulatedState.getLshHashFactory().getNewLSHHash();
        hash.setObject(dataObjects[0].toByteArray(), n_blocks);
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
    void getBlockAt_SingleBlock(){
        int n_blocks = 1;
        int position = 0;
        LSHHash hash = simulatedState.getLshHashFactory().getNewLSHHash();
        hash.setObject(dataObjects[0].toByteArray(), n_blocks);
        LSHHashBlock[] blocks = hash.getBlocks();
        LSHHashBlock block = hash.getBlockAt(position);
        assertArrayEquals(blocks[position].lshBlock(), block.lshBlock());
    }

    @Test
    void getBlockAt_AllBlocks(){
        int n_blocks = 6;
        LSHHash hash = simulatedState.getLshHashFactory().getNewLSHHash();
        hash.setObject(dataObjects[0].toByteArray(), n_blocks);
        LSHHashBlock[] blocks = hash.getBlocks();
        for (int i = 0; i<n_blocks; i++) {
            LSHHashBlock block = hash.getBlockAt(i);
            assertArrayEquals(blocks[i].lshBlock(), block.lshBlock());
        }
        assertEquals( n_blocks, blocks.length );
    }

    //Component Unit Testing
    /**
     * Tests the create ngram function by comparing its result to a hand made one
     * @throws Exception if an exception is thrown the test fails
     */
    @Test
    void create_ngrams() throws Exception {
        byte[] test_data = new byte[]{1,2,3,4,5,6,7,8,9};
        Set<Integer> expected_result = new HashSet<>();
        expected_result.add( 0x00010203 );
        expected_result.add( 0x00020304 );
        expected_result.add( 0x00030405 );
        expected_result.add( 0x00040506 );
        expected_result.add( 0x00050607 );
        expected_result.add( 0x00060708 );
        expected_result.add( 0x00070809 );

        //Change config to match test requirements
        simulatedState.getConfigurator().setConfig("NGRAMS_LEVEL", "3");

        Set<Integer> resulting_data = NgramProcessor.create_ngrams(test_data, simulatedState);

        assertEquals( expected_result.size(), resulting_data.size() );
        assertTrue( resulting_data.containsAll( expected_result ) );

    }
}
