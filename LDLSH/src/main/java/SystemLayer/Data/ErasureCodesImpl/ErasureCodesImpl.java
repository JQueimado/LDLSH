package SystemLayer.Data.ErasureCodesImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.ErasureBlock;
import SystemLayer.SystemExceptions.IncompleteBlockException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serial;

public abstract class ErasureCodesImpl implements ErasureCodes{

    public DataContainer appContext;
    public ErasureBlock[] erasureBlocks;
    public int number_of_blocks;
    public int total_blocks;

    /**
     * Erasure codes super constructor
     * @param appContext application context
     */
    public ErasureCodesImpl( DataContainer appContext ){
        this.appContext = appContext;
        this.total_blocks = appContext.getNumberOfBands();
        this.erasureBlocks = new ErasureBlock[total_blocks];
        this.number_of_blocks = 0;
    }

    //Abstract methods
    @Override
    public abstract void encodeDataObject(byte[] object, int n_blocks) throws Exception ;

    @Override
    public abstract byte[] decodeDataObject() throws IncompleteBlockException;

    //Standard methods
    @Override
    public void addBlockAt(ErasureBlock erasureBlock) {
        if( erasureBlocks[erasureBlock.getPosition()] == null )
            number_of_blocks++;
        erasureBlocks[erasureBlock.getPosition()] = erasureBlock;
    }

    @Override
    public ErasureBlock[] getErasureBlocks() {
        return erasureBlocks;
    }

    @Override
    public ErasureBlock getBlockAt(int position) {
        return erasureBlocks[position];
    }

    @Override
    public int compareTo( @NotNull ErasureCodes o) {
        for ( int i = 0; i<erasureBlocks.length; i++ ){
            ErasureBlock A_block = erasureBlocks[i];
            ErasureBlock B_block = o.getBlockAt(i);

            if( A_block == null || B_block == null )
                return -1;

            if( !A_block.equals(B_block))
                return -1;
        }
        return 0;
    }

    @Override
    public boolean equals( @NotNull Object obj) {
        try {
            ErasureCodesImpl temp = (ErasureCodesImpl) obj;
            return this.compareTo(temp) == 0;
        }catch (Exception e){
            return false;
        }
    }

    //Auxiliary classes
    /**
     * Adds or removes zero padding from a given byte array
     * @param data input data
     * @return new processed byte array
     */
    public byte[] addPadding( byte[] data, int n_blocks ) throws Exception {

        //Add space for padding var
        byte[] paddingObject = new byte[data.length+1];
        System.arraycopy(data, 0, paddingObject, 1, data.length);
        data = paddingObject;

        //Calculate padding size
        int paddingSize = 0;
        if( data.length % n_blocks != 0 ){
            int sizeFittedByBlocks = (data.length / n_blocks) * n_blocks;
            int sizePlusExtraBlock = sizeFittedByBlocks + n_blocks;
            paddingSize = sizePlusExtraBlock - data.length;
        }

        //Set padding size
        if( paddingSize > 255 )
            throw new Exception("Invalid Padding size: " + paddingSize);
        data[0] = (byte) paddingSize;

        //Copy to new Array
        byte[] result = new byte[data.length + paddingSize];
        System.arraycopy(data, 0, result, 0, data.length);
        return result;
    }

    public byte[] removePadding( byte[] data ){

        //Get padding size
        int paddingSize = data[0] & 0xFF;
        byte[] result = new byte[ data.length - paddingSize - 1 ];

        System.arraycopy(data, 1, result, 0, result.length);
        return result;
    }

    //Serialization
    @Serial
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeObject(erasureBlocks);
        stream.writeInt(number_of_blocks);
        stream.writeObject(total_blocks);
    }

    @Serial
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        erasureBlocks = (ErasureBlock[]) stream.readObject();
        number_of_blocks = stream.readInt();
        total_blocks = stream.readInt();
    }

}
