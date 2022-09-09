package SystemLayer.Data.DataUnits;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.Arrays;

public record ObjectMultimapValue( byte[] object ) implements MultiMapValue {
    @Override
    public boolean equals(Object obj) {
        if( obj.getClass() != ObjectMultimapValue.class )
            return false;

        ObjectMultimapValue objectMultimapValue = (ObjectMultimapValue) obj;
        return Arrays.equals(object, objectMultimapValue.object);
    }
}
