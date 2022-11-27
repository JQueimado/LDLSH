package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.SystemExceptions.UnknownConfigException;
import info.debatty.java.lsh.MinHash;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

public class JavaMinHash extends LSHHashImpl{

    //constants
    public static final String THRESHOLD = "THRESHOLD";
    public static final String LSH_SEED = "LSH_SEED";

    // Auxiliary methods
    /**
     * Transforms an object into a set of integers (required to calculate signatures)
     * @param object Array required
     * @return Set of integers
     */
    public static Set<Integer> toIntSet( byte[] object ){
        Set<Integer> values = new HashSet<>();
        int i;
        int j = object.length;
        for (i = 0; i<j; i++ ){
            int value = object[i];
            values.add( value );
        }
        return values;
    }

    /**
     * Transforms a Array of integers into an Array of bytes
     * @param ints Input int Array
     * @return Resulting Array of bytes
     */
    public static byte[] toByteArray(int[] ints ){
        ByteBuffer byteBuffer = ByteBuffer.allocate(ints.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(ints);
        return byteBuffer.array();
    }

    //Objects
    public JavaMinHash(DataContainer dataContainer){
        super(dataContainer);
    }

    public JavaMinHash(DataObject<?> object, int n_blocks, DataContainer dataContainer){
        super(dataContainer);
        setObject(object.toByteArray(), n_blocks);
    }

    @Override
    public void setObject( byte[] object, int n_blocks ){
       this.data = getSignature( toIntSet( object ) );
    }

    /**
     * Given an Array of bytes, calculates it respective MinHash signature.
     * @param data Input Array.
     * @return Array representation of a signature.
     */
    protected byte[] getSignature( Set<Integer> data ){
        try {
            Configurator configurator = appContext.getConfigurator();

            //Get Accuracy
            String accuracy_string = configurator.getConfig(JavaMinHash.THRESHOLD);
            double threshold;
            if (accuracy_string.isBlank())
                throw new UnknownConfigException(THRESHOLD, accuracy_string);
            threshold = Double.parseDouble(accuracy_string);

            //Get Seed
            String seed_string = configurator.getConfig(LSH_SEED);
            long seed;
            if (seed_string.isBlank())
                throw new UnknownConfigException(LSH_SEED, seed_string);
            seed = Long.parseLong(seed_string);

            //Setup Encoder
            int s = appContext.getNumberOfBands();
            int signature_size = ( (int) Math.ceil(Math.log(1.0 / s) / Math.log(threshold)) + 1 ) * s;

            MinHash minHash = new MinHash(signature_size, data.size(), seed);

            //Create Signature
            int[] signature = minHash.signature(data);

            return toByteArray(signature);

        } catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
            return null;
        }
    }
}
