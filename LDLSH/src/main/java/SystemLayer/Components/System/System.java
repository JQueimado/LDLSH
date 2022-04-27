package SystemLayer.Components.System;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public interface System {
    void insert(DataObject object ) throws Exception;
    DataObject query( DataObject queryObject ) throws Exception ;
}
