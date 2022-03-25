package LSHTests;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import info.debatty.java.lsh.MinHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JavaMinHashSimilarityTests {

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

    void similarityTest(DataObject dataObject1, DataObject dataObject2, double expected_similarity ) throws Exception {
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

    @Test
    void getValuesSimilarity_01() throws Exception {
        similarityTest(dataObjects[0], dataObjects[1], 0.8 );
    }

    @Test
    void getValuesSimilarity_02() throws Exception {
        similarityTest(dataObjects[0], dataObjects[2], 1);
    }

    @Test
    void getValuesSimilarity_03() throws Exception {
        similarityTest(dataObjects[0], dataObjects[3], 0.2);
    }

    //Auxiliary methods
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
