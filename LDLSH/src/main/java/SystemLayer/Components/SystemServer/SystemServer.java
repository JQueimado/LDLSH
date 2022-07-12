package SystemLayer.Components.SystemServer;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

public interface SystemServer {
    /**
     * Submits an insert Task to the system
     * @param object object to insert
     * @return same object if the task was succesfull
     * @throws Exception if an error occurs
     */
    ListenableFuture<DataObject> insert(DataObject object ) throws Exception;

    /**
     * Submits a Query task to the system
     * @param queryObject query object
     * @return query result
     * @throws Exception if an error occurs
     */
    ListenableFuture<DataObject> query(DataObject queryObject ) throws Exception ;

    /**
     * Returns a new empty data object according to the System's specification
     * @return new DataObject
     * @throws Exception If an error occurs
     */
    DataObject newDataObject() throws Exception;
}
