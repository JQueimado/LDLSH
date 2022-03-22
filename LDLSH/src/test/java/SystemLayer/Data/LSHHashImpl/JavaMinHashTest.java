package SystemLayer.Data.LSHHashImpl;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
    void setObject() {
    }

    @Test
    void getValues() throws Exception {
        for (DataObject dataObject : dataObjects) {
            JavaMinHash hash = new JavaMinHash(dataObject, simulatedState);
            byte[] signature = hash.getValues();
            printArray( toIntArray( signature ) );
        }
    }

    @Test
    void getValuesSimilarity_01() throws Exception {
        JavaMinHash hash = new JavaMinHash(dataObjects[0], simulatedState);
        byte[] signature = hash.getValues();
        int[] ints = toIntArray(signature);

        //Additional Data 1
        System.out.println("Data0 int:");
        printArray( toIntArray( dataObjects[0].toByteArray() ) );
        printArray(ints);

        JavaMinHash hash2 = new JavaMinHash(dataObjects[1], simulatedState);
        byte[] signature2 = hash2.getValues();
        int[] ints2 = toIntArray(signature2);

        //Additional Data 2
        System.out.println("Data1 int:");
        printArray( toIntArray( dataObjects[1].toByteArray() ) );
        printArray(ints2);

        double similarity = JavaMinHash.getMinHash().similarity(ints, ints2);
        System.out.printf( "Similarity: %f ", similarity );
    }

    @Test
    void getValuesSimilarity_02() throws Exception {
        JavaMinHash hash = new JavaMinHash(dataObjects[0], simulatedState);
        byte[] signature = hash.getValues();
        int[] ints = toIntArray(signature);

        //Additional Data 1
        System.out.println("Data0 int:");
        printArray( toIntArray( dataObjects[0].toByteArray() ) );
        printArray(ints);

        JavaMinHash hash2 = new JavaMinHash(dataObjects[2], simulatedState);
        byte[] signature2 = hash2.getValues();
        int[] ints2 = toIntArray(signature2);

        //Additional Data 2
        System.out.println("Data1 int:");
        printArray( toIntArray( dataObjects[2].toByteArray() ) );
        printArray(ints2);

        double similarity = JavaMinHash.getMinHash().similarity(ints, ints2);
        System.out.printf( "Similarity: %f ", similarity );
    }

    @Test
    void getValuesSimilarity_03() throws Exception {
        JavaMinHash hash = new JavaMinHash(dataObjects[0], simulatedState);
        byte[] signature = hash.getValues();
        int[] ints = toIntArray(signature);

        //Additional Data 1
        System.out.println("Data0 int:");
        printArray( toIntArray( dataObjects[0].toByteArray() ) );
        printArray(ints);

        JavaMinHash hash2 = new JavaMinHash(dataObjects[3], simulatedState);
        byte[] signature2 = hash2.getValues();
        int[] ints2 = toIntArray(signature2);

        //Additional Data 2
        System.out.println("Data1 int:");
        printArray( toIntArray( dataObjects[3].toByteArray() ) );
        printArray(ints2);

        double similarity = JavaMinHash.getMinHash().similarity(ints, ints2);
        System.out.printf( "Similarity: %f ", similarity );
    }

    @Test
    void getBlocks() {
    }

    //Auxiliary methods
    private void printArray(int[] array ){
        System.out.print("[");
        for (int i : array){
            System.out.print(i);
            System.out.print(",");
        }
        System.out.println("]");
    }

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
}