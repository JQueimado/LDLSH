package SystemLayer.Data.DataUnits;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Representation of a signature block
 *
 * @param lshBlock signature block's data
 */
public record LSHHashBlock(int[] lshBlock) implements Comparable<LSHHashBlock>, Serializable {
    @Override
    public int compareTo(LSHHashBlock o) {
        return Arrays.compare(lshBlock, o.lshBlock);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != LSHHashBlock.class)
            return false;
        LSHHashBlock conv = (LSHHashBlock) obj;
        return Arrays.equals(this.lshBlock, conv.lshBlock);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.lshBlock);
    }

    /**/
}
