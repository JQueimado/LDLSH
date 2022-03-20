package SystemLayer.Data.LSHHashImpl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Set;

public interface LSHHash<T> extends Serializable {
    void setValues( T[] values );
    T[] getValues();
    LSHBlock<T>[] getBlocks(int n_blocks);

    interface LSHBlock<T>{
        void setValues( T[] values );
        T[] getValues();
    }
}
