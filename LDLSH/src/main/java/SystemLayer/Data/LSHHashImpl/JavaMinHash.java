package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import info.debatty.java.lsh.MinHash;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.MissingFormatArgumentException;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaMinHash implements LSHHash{

    /* Global to all objects */

    //constants
    private static final String ERROR = "ERROR";
    private static final String VECTOR_DIMENSIONS = "VECTOR_DIMENSIONS";

    //Static Methods
    private static MinHash minHash = null;
    private static DataContainer dataContainer = null;

    public static MinHash getMinHash() throws Exception {
        if( minHash == null ) {
            if (dataContainer == null){
                throw new Exception("No DataContainer was provided");
            }

            Configurator configurator = dataContainer.getConfigurator();

            //Get Accuracy
            String accuracy_string = configurator.getConfig(ERROR);
            double accuracy_error;
            if (accuracy_string.isBlank())
                throw new Exception("JavaMinHash requires ERROR configuration");
            else
                accuracy_error = Double.parseDouble(accuracy_string);

            //Get Vector dimensions
            String vector_dimensions_string = configurator.getConfig(VECTOR_DIMENSIONS);
            int vector_dimensions;
            if (vector_dimensions_string.isBlank())
                throw new Exception("JavaMinHash requires VECTOR_DIMENSIONS configuration");
            else
                vector_dimensions = Integer.parseInt(vector_dimensions_string);

            //Build MinHash module
            minHash = new MinHash(accuracy_error, vector_dimensions);
        }
        return minHash;
    }

    /* Individual Objects */

    //Values
    private byte[] data;

    //Constructors
    public JavaMinHash(DataObject dataObject, DataContainer dataContainer ) throws Exception{
        JavaMinHash.dataContainer = dataContainer;
        this.setObject(dataObject);
    }

    public JavaMinHash(DataContainer dataContainer){
        JavaMinHash.dataContainer = dataContainer;
        this.data = null;
    }

    //Auxiliary methods
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

    private byte[] toByteArray(int[] ints ){
        ByteBuffer byteBuffer = ByteBuffer.allocate(ints.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(ints);
        return byteBuffer.array();
    }

    @Override
    public void setObject( DataObject object ){
        try {

            //GetSignature
            byte[] data = object.toByteArray();
            Set<Integer> intData = toIntSet(data);
            int[] signature = JavaMinHash.getMinHash().signature(intData);
            this.data = toByteArray(signature);

        }catch (Exception e){
            System.out.println("ERROR: "+ e.getMessage());
            this.data = null;
        }
    }

    @Override
    public byte[] getValues() {
        return data;
    }

    @Override
    public byte[][] getBlocks(int n_blocks) {
        return null;
    }
}
