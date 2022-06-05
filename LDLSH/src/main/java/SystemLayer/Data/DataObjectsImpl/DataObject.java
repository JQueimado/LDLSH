package SystemLayer.Data.DataObjectsImpl;

import java.io.Serializable;

public interface DataObject<T> extends Serializable, Comparable<DataObject<T>> {
    /**
     * Returns the object value
     * @return object value (as its primitive type)
     */
    T getValues();

    /**
     * Sets the object value
     * @param values object value
     */
    void setValues( T values );

    /**
     * Sets the object value as a byte array
     * @param array object value
     */
    void setByteArray( byte[] array );

    /**
     * Returns the object value as a byte array
     * @return object value
     */
    byte[] toByteArray();

    /**
     * Given an input vector size, returns the expected byte size for that vector
     * @param dimensions vector dimensions
     * @return byte size
     */
    int objectByteSize( int dimensions );
}
