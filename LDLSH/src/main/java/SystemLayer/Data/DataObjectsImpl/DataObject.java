package SystemLayer.Data.DataObjectsImpl;

import java.io.Serializable;

public interface DataObject<T> extends Serializable, Comparable<DataObject<T>> {
    T getValues();
    void setValues( T values );
    byte[] toByteArray();
}
