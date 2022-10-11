package SystemLayer.Data.DataUnits;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import org.jetbrains.annotations.NotNull;

import java.io.ObjectInputStream;
import java.util.Arrays;

public record ObjectMultimapValue( byte[] object ) implements MultiMapValue {
    @Override
    public boolean equals(Object obj) {
        if( obj.getClass() != ObjectMultimapValue.class )
            return false;

        ObjectMultimapValue objectMultimapValue = (ObjectMultimapValue) obj;
        return Arrays.equals(object, objectMultimapValue.object);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(object);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if( !(o instanceof ObjectMultimapValue) )
            return -1;
        return Arrays.compare( this.object, ((ObjectMultimapValue) o).object);
    }
}
