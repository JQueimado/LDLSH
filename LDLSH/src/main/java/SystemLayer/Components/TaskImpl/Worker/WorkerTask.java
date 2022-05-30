package SystemLayer.Components.TaskImpl.Worker;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.concurrent.Callable;

public interface WorkerTask extends Callable<DataObject> {

}
