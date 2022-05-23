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

public class JavaMinHash implements LSHHash{

    /* Global to all objects */

    //constants
    private static final String ERROR = "ERROR";
    private static final String VECTOR_DIMENSIONS = "VECTOR_DIMENSIONS";
    private static final String LSH_SEED = "LSH_SEED";

    //Static Methods
    private static MinHash minHash = null;
    private static DataContainer dataContainer = null;

    public static MinHash getMinHash( DataContainer dataContainer ) throws Exception {
        JavaMinHash.dataContainer = dataContainer;
        return getMinHash();
    }

    public static MinHash getMinHash() throws Exception { //l-n+1
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

            //Get Seed
            String seed_string = configurator.getConfig(LSH_SEED);
            long seed;
            if (seed_string.isBlank())
                minHash = new MinHash(accuracy_error, vector_dimensions);
            else {
                seed = Long.parseLong(vector_dimensions_string);
                minHash = new MinHash(accuracy_error, vector_dimensions, seed);
            }

        }
        return minHash;
    }

    /* Individual Objects */

    //Values
    private byte[] data;
    private LSHHashBlock[] blocks;

    //Constructors
    public JavaMinHash(DataObject dataObject, int n_blocks, DataContainer dataContainer ){
        JavaMinHash.dataContainer = dataContainer;
        setObject(dataObject, n_blocks);
    }

    public JavaMinHash(DataContainer dataContainer){
        JavaMinHash.dataContainer = dataContainer;
        this.data = null;
    }

    //Auxiliary methods
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

    private byte[] toByteArray(int[] ints ){
        ByteBuffer byteBuffer = ByteBuffer.allocate(ints.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(ints);
        return byteBuffer.array();
    }

    @Override
    public void setObject( DataObject object, int n_blocks ){
        try {

            //GetSignature
            byte[] data = object.toByteArray();
            Set<Integer> intData = toIntSet(data);
            int[] signature = JavaMinHash.getMinHash().signature(intData);
            this.data = toByteArray(signature);

            blocks = createBlocks(this.data, n_blocks);

        }catch (Exception e){
            System.out.println("ERROR: "+ e.getMessage());
            e.printStackTrace();
            this.data = null;
            this.blocks = null;
        }
    }

    @Override
    public byte[] getSignature() {
        return data;
    }

    @Override
    public LSHHashBlock[] getBlocks() {
        return blocks;
    }

    @Override
    public LSHHashBlock getBlockAt(int position) {
        return blocks[position];
    }

    //Auxiliary
    private LSHHashBlock[] createBlocks( byte[] signature, int n_blocks ){
        final LSHHashBlock[] blockArray = new LSHHashBlock[n_blocks]; //Array of blocks to be returned
        LSHHashBlock blockDump; //temporary LSHHashBlock pointer
        int block_count = 0; //block counter

        final int signature_length = signature.length; //Signature size
        int signature_counter; //Signature position counter

        final int block_size = Math.floorDiv(signature_length, n_blocks) + 1; //block size topped
        byte[] current_block = new byte[block_size]; //temporary block
        int block_size_counter=0; //Block position counter

        for (signature_counter = 0; signature_counter<signature_length; signature_counter++) {
            current_block[block_size_counter]= signature[signature_counter]; //Assign value
            block_size_counter++;

            if (block_size_counter >= block_size) {
                blockDump = new LSHHashBlock(current_block); //Create block

                if (block_size > signature_length - signature_counter)
                    current_block = new byte[signature_length - signature_counter - 1]; //creates smaller array
                else
                    current_block = new byte[block_size_counter]; //creates block sized array

                block_size_counter = 0;

                blockArray[block_count] = blockDump; //Assigns new Block
                block_count++;
            }
        }

        if ( block_count < n_blocks ){ //Dump remaining values into the last block
            blockDump = new LSHHashBlock(current_block);
            blockArray[block_count] = blockDump;
        }

        return blockArray;
    }

    @Override
    public int compareTo( LSHHash o ) {
        return Arrays.compare(this.getSignature(), o.getSignature());
    }
}
