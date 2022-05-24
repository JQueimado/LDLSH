package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import info.debatty.java.lsh.MinHash;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JavaMinHash extends LSHHashImpl{

    /** Static methods, variables and constants **/

    //constants
    public static final String ERROR = "ERROR";
    public static final String VECTOR_DIMENSIONS = "VECTOR_DIMENSIONS";
    public static final String LSH_SEED = "LSH_SEED";

    private static MinHash minHash = null;

    /**
     * Prepares the MinHash encoder
     * @param error minhash required error
     * @param vector_dimensions expected vector dimensions
     * @param seed RNG seed
     */
    public static void setupMinHash(
            double error,
            int vector_dimensions,
            long seed
    ) { //l-n+1
        minHash = new MinHash(error, vector_dimensions, seed);
    }

    /**
     * Checks if the MinHash encode has been set
     * @return true if its already set or false if not
     */
    public static boolean isSet(){
        return minHash != null;
    }

    /**
     * Returns the MinHash encoder
     * @return returns the encoder if its already setup, null if not
     */
    public static MinHash getMinHash(){
        return minHash;
    }

    /** Objects **/
    //Constructors
    public JavaMinHash(DataContainer dataContainer){
        super(dataContainer);
    }

    public JavaMinHash(DataObject object, int n_blocks, DataContainer dataContainer){
        super(dataContainer);
        setObject(object, n_blocks);
    }

    @Override
    public void setObject( DataObject object, int n_blocks ){
       this.data = getSignature( object.toByteArray() );
       this.blocks = createBlocks(this.data, n_blocks);
    }

    /** Auxiliary **/
    /**
     * Transforms a byte array into a set of integers (required to calculate signatures)
     * @param bytes Array required
     * @return Set of integers
     */
    private Set<Integer> toIntSet( byte[] bytes ){
        Set<Integer> values = new HashSet<>();
        int i;
        int j = bytes.length;
        for (i = 0; i<j; i++ ){
            int value = bytes[i];
            values.add( value );
        }
        return values;
    }

    /**
     * Transforms a Array of integers into an Array of bytes
     * @param ints Input int Array
     * @return Resulting Array of bytes
     */
    private byte[] toByteArray(int[] ints ){
        ByteBuffer byteBuffer = ByteBuffer.allocate(ints.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(ints);
        return byteBuffer.array();
    }

    /**
     * Given an Array of bytes, calculates it respective MinHash signature.
     * @param data Input Array.
     * @return Array representation of a signature.
     */
    protected byte[] getSignature( byte[] data ){
        try {
            //GetSignature
            Set<Integer> intData = toIntSet(data);
            int[] signature = JavaMinHash.getMinHash().signature(intData);
            return toByteArray(signature);
        } catch (Exception e) {
            System.out.println("ERROR: "+ e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
