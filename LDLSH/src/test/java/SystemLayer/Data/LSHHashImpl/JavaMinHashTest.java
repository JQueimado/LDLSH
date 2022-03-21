package SystemLayer.Data.LSHHashImpl;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import info.debatty.java.lsh.MinHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JavaMinHashTest {

    DataContainer simulatedState;
    DataObject[] dataObjects;

    @BeforeEach
    void simulateStateAndVectors() throws IOException {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("ERROR", "0.5");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");

        //Vectors
        String data_object_type = "STRING";
        DataObjectFactory dataObjectFactory = simulatedState.getDataObjectFactory();
        dataObjects = new DataObject[3];

        dataObjects[0] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[0].setValues("12345");

        dataObjects[1] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[1].setValues("12355");

        dataObjects[2] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[2].setValues("12345");
    }

    @Test
    void setObject() {
    }

    private void printArray(int[] array ){
        System.out.print("[");
        for (int i : array){
            System.out.print(i);
            System.out.print(",");
        }
        System.out.println("]");
    }

    private int[] toIntArray( byte[] array ){
        IntBuffer intBuffer = ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        int[] ints = new int[intBuffer.remaining()];
        intBuffer.get(ints);
        return ints;
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
    void getValuesSimilarity() throws Exception {
        JavaMinHash hash = new JavaMinHash(dataObjects[0], simulatedState);
        byte[] signature = hash.getValues();
        int[] ints = toIntArray(signature);
        printArray(ints);

        JavaMinHash hash2 = new JavaMinHash(dataObjects[1], simulatedState);
        byte[] signature2 = hash2.getValues();
        int[] ints2 = toIntArray(signature2);
        printArray(ints2);

        System.out.println( "Similarity: " + JavaMinHash.getMinHash().similarity(ints, ints2) );
    }

    @Test
    void getBlocks() {
    }
}