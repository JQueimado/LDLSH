package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.SystemExceptions.UnknownConfigException;
import info.debatty.java.lsh.MinHash;

import javax.naming.ConfigurationException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JavaMinHash extends LSHHashImpl{

    //constants
    public static final String ERROR = "ERROR";
    public static final String LSH_SEED = "LSH_SEED";

    public JavaMinHash(DataContainer dataContainer){
        super(dataContainer);
    }

    public JavaMinHash(DataObject object, int n_blocks, DataContainer dataContainer){
        super(dataContainer);
        setObject(object.toByteArray(), n_blocks);
    }

    @Override
    public void setObject( byte[] object, int n_blocks ){
       this.data = getSignature( toIntSet( object ) );
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
    protected byte[] getSignature( Set<Integer> data ){
        try {
            Configurator configurator = appContext.getConfigurator();

            //Get Accuracy
            String accuracy_string = configurator.getConfig(JavaMinHash.ERROR);
            double accuracy_error;
            if (accuracy_string.isBlank())
                throw new UnknownConfigException(ERROR, accuracy_string);
            accuracy_error = Double.parseDouble(accuracy_string);

            //Get Seed
            String seed_string = configurator.getConfig(LSH_SEED);
            long seed;
            if (seed_string.isBlank())
                throw new UnknownConfigException(LSH_SEED, seed_string);
            seed = Long.parseLong(seed_string);

            //Setup Encoder
            MinHash minHash = new MinHash(accuracy_error, data.size(), seed);

            //Create Signature
            int[] signature = minHash.signature(data);

            return toByteArray(signature);

        } catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
            return null;
        }
    }
}
