package SystemLayer.Data.LSHHashImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.LSHHashBlock;

import java.io.IOException;
import java.util.Arrays;

public abstract class LSHHashImpl implements LSHHash{

    public static DataContainer appContext;
    public int[] data;
    public LSHHashBlock[] blocks;

    /**
     * Global LSHHashImpl constructor
     * @param appContext application context
     */
    public LSHHashImpl(DataContainer appContext){
        LSHHashImpl.appContext = appContext;
    }

    //Abstract methods
    @Override
    public abstract void setObject(byte[] object, int n_blocks);

    //Standard Methods
    @Override
    public int[] getSignature() {
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

    @Override
    public int compareTo(LSHHash o) {
        return Arrays.compare(this.getSignature(), o.getSignature());
    }

    @Override
    public boolean equals(Object obj) {
        LSHHashImpl temp = (LSHHashImpl) obj;
        return this.compareTo(temp) == 0;
    }

    // Auxiliary
    /**
     * Divides a given signature int a set of signature blocks
     * @param signature given signature
     * @param n_blocks number of divisions
     * @return Array of signature blocks
     */
    public LSHHashBlock[] createBlocks( int[] signature, int n_blocks ){
        final LSHHashBlock[] blockArray = new LSHHashBlock[n_blocks]; //Array of blocks to be returned
        LSHHashBlock blockDump; //temporary LSHHashBlock pointer
        int block_count = 0; //block counter

        final int signature_length = signature.length; //Signature size
        int signature_counter; //Signature position counter

        final int block_size = Math.floorDiv(signature_length, n_blocks) + 1; //block size topped
        int[] current_block = new int[block_size]; //temporary block
        int block_size_counter=0; //Block position counter

        for (signature_counter = 0; signature_counter<signature_length; signature_counter++) {
            current_block[block_size_counter]= signature[signature_counter]; //Assign value
            block_size_counter++;

            if (block_size_counter >= block_size) {
                blockDump = new LSHHashBlock(current_block); //Create block

                if (block_size > signature_length - signature_counter)
                    current_block = new int[signature_length - signature_counter - 1]; //creates smaller array
                else
                    current_block = new int[block_size_counter]; //creates block sized array

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

    //Serialization
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeObject(data);
        stream.writeObject(blocks);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        data = (int[]) stream.readObject();
        blocks = (LSHHashBlock[]) stream.readObject();
    }

}
