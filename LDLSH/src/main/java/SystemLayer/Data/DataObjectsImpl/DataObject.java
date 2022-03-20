package SystemLayer.Data.DataObjectsImpl;

import java.io.Serializable;

public interface DataObject extends Serializable {
    Object getValues();
    void setValues( Object values );
    byte[] toByteArray();
}
