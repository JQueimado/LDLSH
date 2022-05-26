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
    public static final String LSH_SEED = "LSH_SEED";

    private static MinHash minHash = null;

    protected static boolean isSetup(){
        return minHash != null;
    }

    /**
     * Returns the MinHash encoder
     * @return returns the encoder if its already setup, null if not
     */
    public static MinHash getMinHash(){
        return minHash;
    }

    // Objects context
    // Object Constructor
    public JavaMinHash(DataContainer dataContainer){
        super(dataContainer);
    }

    public JavaMinHash(DataObject object, int n_blocks, DataContainer dataContainer){
        super(dataContainer);
        setObject(object.toByteArray(), n_blocks);
    }

    @Override
    public void setObject( byte[] object, int n_blocks ){
        if ( !isSetup() )
            setupMinHash(object.length, appContext);

       this.data = getSignature( object );
       this.blocks = createBlocks(this.data, n_blocks);
    }

    // Auxiliary methods
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

    /**
     * Prepares the MinHash encoder
     * @param appContext context containing the configurations
     */
    protected void setupMinHash( int vector_dimensions, DataContainer appContext ){ //l-n+1
        try {

            if (appContext == null) {
                throw new Exception("No DataContainer was provided");
            }

            Configurator configurator = appContext.getConfigurator();

            //Get Accuracy
            String accuracy_string = configurator.getConfig(JavaMinHash.ERROR);
            double accuracy_error;
            if (accuracy_string.isBlank())
                throw new Exception("JavaMinHash requires ERROR configuration");
            else
                accuracy_error = Double.parseDouble(accuracy_string);

            //Get Seed
            String seed_string = configurator.getConfig(LSH_SEED);
            long seed;
            if (seed_string.isBlank())
                throw new Exception("JavaMinHash requires " + LSH_SEED + " configuration");
            seed = Long.parseLong(seed_string);

            //Setup
            minHash = new MinHash(accuracy_error, vector_dimensions, seed);
        } catch (Exception e){
            System.out.println( e.getMessage() );
            e.printStackTrace();
            minHash = null;
        }
    }
}
