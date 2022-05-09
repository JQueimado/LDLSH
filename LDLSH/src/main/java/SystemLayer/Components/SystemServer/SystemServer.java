package SystemLayer.Components.SystemServer;

import SystemLayer.Data.DataObjectsImpl.DataObject;

public interface SystemServer {
    void insert(DataObject object ) throws Exception;
    DataObject query( DataObject queryObject ) throws Exception ;
}
