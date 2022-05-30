package SystemLayer.Components.SystemServer;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.concurrent.Future;

public interface SystemServer {
    Future insert(DataObject object ) throws Exception;
    DataObject query( DataObject queryObject ) throws Exception ;
}
