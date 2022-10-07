package SystemLayer.Data.LSHTests;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import SystemLayer.Data.LSHHashImpl.JavaMinHashNgrams;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.SystemMain.SystemMain;
import info.debatty.java.lsh.MinHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaMinHashNgramsTests {

    DataContainer simulatedState;
    DataObject<String>[] dataObjects;

    public void set_objects(){
        String file = "data_sets/jqueimado_QS_1000.txt";
        try{
            ArrayList<DataObject> list = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader( new FileReader(file) );
            String line;

            while( (line = bufferedReader.readLine()) != null ){
                DataObject object = simulatedState.getDataObjectFactory().getNewDataObject();
                object.setByteArray( line.getBytes(StandardCharsets.UTF_8) );
                list.add(object);
            }
            dataObjects = list.toArray( new DataObject[list.size()] );

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @BeforeEach
    void simulateStateAndVectors() throws Exception {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("OBJECT_TYPE", "STRING");
        configurator.setConfig("ERROR", "0.05");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "11111");
        configurator.setConfig("LSH_HASH", "JAVA_MINHASH_NGRAMS");
        configurator.setConfig("NGRAMS_LEVEL", "2");

        //Vectors

        set_objects();

        /*
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
         */
    }

    /* Similarity Tests */

    /**
     * Evaluates the similarity between 2 data objects against an expected similarity
     * This test only displays the values since LSH is a probabilistic algorithm and results are not accurate.
     * @param dataObject1 data object 1
     * @param dataObject2 data object 2
     */
    void similarityTest(DataObject<String> dataObject1, DataObject<String> dataObject2 )
        throws Exception{
        //Hash 1
        JavaMinHashNgrams hash = (JavaMinHashNgrams) simulatedState.getLshHashFactory().getNewLSHHash();
        hash.setObject(dataObject1.toByteArray(), 2);
        int[] signature = hash.getSignature();

        //Hash 2
        JavaMinHashNgrams hash2 = (JavaMinHashNgrams) simulatedState.getLshHashFactory().getNewLSHHash();
        hash2.setObject(dataObject2.toByteArray(), 2);
        int[] signature2 = hash2.getSignature();

        //Jaccard similarity
        double error = Double.parseDouble( simulatedState.getConfigurator().getConfig("ERROR") );
        long seed = Long.parseLong( simulatedState.getConfigurator().getConfig("LSH_SEED") );
        MinHash minHash = new MinHash(error, dataObject1.objectByteSize(5),seed);
        double similarity = minHash.similarity(signature, signature2);

        Set<Integer> obj1_ngram = JavaMinHashNgrams.create_ngrams(dataObject1.toByteArray(), simulatedState);
        Set<Integer> obj2_ngram = JavaMinHashNgrams.create_ngrams( dataObject2.toByteArray(), simulatedState);

        double jaccard_similarity = MinHash.jaccardIndex( obj1_ngram, obj2_ngram );

        //Output
        System.out.printf(
                """
                Similarity test between "%s" and "%s":
                \t-Actual Similarity: %f
                \t-Jaccard Similarity: %f
                """,
                dataObject1.getValues(),
                dataObject2.getValues(),
                similarity,
                jaccard_similarity
        );
    }

    //Similarity tests
    @Test
    void getValuesSimilarity_01() throws Exception {
        similarityTest(dataObjects[0], dataObjects[1]);
    }

    @Test
    void getValuesSimilarity_02() throws Exception  {
        similarityTest(dataObjects[0], dataObjects[2]);
    }

    @Test
    void getValuesSimilarity_03() throws Exception  {
        similarityTest(dataObjects[0], dataObjects[3]);
    }

    @Test
    void getValuesSimilarity_04() throws Exception  {
        similarityTest(dataObjects[0], dataObjects[4]);
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

        Set<Integer> resulting_data = JavaMinHashNgrams.create_ngrams(test_data, simulatedState);

        assertEquals( expected_result.size(), resulting_data.size() );
        assertTrue( resulting_data.containsAll( expected_result ) );

    }

}