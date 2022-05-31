package LSHTests;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import info.debatty.java.lsh.MinHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

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

        double similarity = JavaMinHash.getMinHash().similarity(ints, ints2);
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
}
