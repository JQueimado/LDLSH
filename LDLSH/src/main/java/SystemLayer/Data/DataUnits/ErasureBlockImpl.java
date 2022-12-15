package SystemLayer.Data.DataUnits;

import java.util.Arrays;
import java.util.Objects;

public class ErasureBlockImpl implements ErasureBlock {

    byte[] block_data;
    int position;

    /**
     * Object representing a single Erasure code
     *
     * @param block_data erasure code's data
     * @param position   erasure code's position
     */
    public ErasureBlockImpl(byte[] block_data, int position) {
        this.block_data = block_data;
        this.position = position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void setBlock(byte[] block) {
        this.block_data = block;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public byte[] getBlock() {
        return block_data;
    }

    @Override
    public int compareTo(ErasureBlock o) {
        if (position != o.getPosition())
            return -1;
        return Arrays.compare(block_data, o.getBlock());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != ErasureBlockImpl.class)
            return false;
        return this.compareTo((ErasureBlock) obj) == 0;
    }
}
