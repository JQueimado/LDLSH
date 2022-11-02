package SystemLayer.Data.LSHTests;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import SystemLayer.Data.LSHHashImpl.JavaMinHashNgrams;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.DataUnits.LSHHashBlock;
import info.debatty.java.lsh.MinHash;
import io.netty.buffer.ByteBuf;
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
     * @param dataObject1 data object 1
     * @param dataObject2 data object 2
     */
    void similarityTest(DataObject<String> dataObject1, DataObject<String> dataObject2 ) throws Exception{
        LSHHash hash = simulatedState.getLshHashFactory().getNewLSHHash();
        hash.setObject(dataObject1.toByteArray(), 2);
        byte[] signature = hash.getSignature();
        int[] ints = toIntArray(signature);

        LSHHash hash2 = simulatedState.getLshHashFactory().getNewLSHHash();
        hash2.setObject(dataObject2.toByteArray(), 2);
        byte[] signature2 = hash2.getSignature();
        int[] ints2 = toIntArray(signature2);

        double error = Double.parseDouble( simulatedState.getConfigurator().getConfig("ERROR") );
        long seed = Long.parseLong( simulatedState.getConfigurator().getConfig("LSH_SEED") );

        MinHash minHash = new MinHash(error, dataObject1.objectByteSize(5),seed);
        double hash_distance = 1 - minHash.similarity(ints, ints2);

        Set<Integer> dataObject1Ngrams = JavaMinHashNgrams.create_ngrams(dataObject1.toByteArray(), simulatedState);
        Set<Integer> dataObject2Ngrams = JavaMinHashNgrams.create_ngrams(dataObject2.toByteArray(), simulatedState);
        double object_distance = 1 - MinHash.jaccardIndex( dataObject1Ngrams, dataObject2Ngrams );

        System.out.printf(
                """
                Similarity test between "%s" and "%s":
                \t-Object Similarity: %f
                \t-Hash Similarity: %f
                """,
                dataObject1.getValues(),
                dataObject2.getValues(),
                object_distance,
                hash_distance
        );
    }

    //Similarity tests
    @Test
    void getValuesSimilarity_00() throws Exception {
        similarityTest(dataObjects[0], dataObjects[0] );
    }

    @Test
    void getValuesSimilarity_01() throws Exception {
        similarityTest(dataObjects[0], dataObjects[1] );
    }

    @Test
    void getValuesSimilarity_02() throws Exception {
        similarityTest(dataObjects[0], dataObjects[2]);
    }

    @Test
    void getValuesSimilarity_03() throws Exception {
        similarityTest(dataObjects[0], dataObjects[3]);
    }

    //@Test
    //void getValuesSimilarity_04() {
    //    similarityTest(dataObjects[0], dataObjects[4], 0.2);
    //}

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
}
