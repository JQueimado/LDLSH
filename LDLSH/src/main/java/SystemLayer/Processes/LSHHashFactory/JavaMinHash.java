package SystemLayer.Processes.LSHHashFactory;

import Factories.DataFactories.LSHHashFactory;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import info.debatty.java.lsh.MinHash;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


public class JavaMinHash implements LSHHashProcessor{

    //const
    private final String ACCURACY = "ACCURACY";
    private final String VECTOR_DIMENSIONS = "VECTOR_DIMENSIONS";

    //Values
    private MinHash minhash;

    public JavaMinHash( DataContainer dataContainer ) throws Exception {
        Configurator configurator = dataContainer.getConfigurator();

        //Get Accuracy
        String accuracy_string = configurator.getConfig(ACCURACY);
        double accuracy_error;
        if (accuracy_string.isBlank())
            throw new Exception("JavaMinHash requires ACCURACY configuration");
        else
            accuracy_error = Double.parseDouble( accuracy_string );

        //Get Vector dimensions
        String vector_dimensions_string = configurator.getConfig(VECTOR_DIMENSIONS);
        int vector_dimensions;
        if (vector_dimensions_string.isBlank())
            throw new Exception("JavaMinHash requires VECTOR_DIMENSIONS configuration");
        else
            vector_dimensions = Integer.parseInt( vector_dimensions_string );

        //Build MinHash module
        minhash = new MinHash( accuracy_error, vector_dimensions );
    }

    private Set<Integer> toIntSet( byte[] bytes ){
        IntBuffer intBuffer = ByteBuffer.wrap( bytes )
                .order(ByteOrder.BIG_ENDIAN)
                .asIntBuffer();

        int[] intArray = new int[intBuffer.remaining()];
        intBuffer.get(intArray);
        return Arrays.stream(intArray)
                .boxed()
                .collect( Collectors.toSet() );
    }

    private Integer[] intToInteger( int[] ints ){
        Integer[] integers = new Integer[ints.length];
        for (int i = 0; i <ints.length; i++){
            integers[i] = ints[i];
        }
        return integers;
    }

    public LSHHash<Integer> getLSH(DataObject dataObject, DataContainer dataContainer) {
        //GetSignature
        byte[] data = dataObject.toByteArray();
        Set<Integer> intData = toIntSet(data);
        int[] signature = minhash.signature(intData);

        //Create LSHHash
        LSHHashFactory lshHashFactory = dataContainer.getLshHashFactory();
        LSHHash<Integer> lshHash = lshHashFactory.getNewLSHHash("INT_ARRAY_HASH");
        lshHash.setValues(intToInteger(signature));

        return lshHash;
    }
}
