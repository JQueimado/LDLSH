package SystemLayer.Components.TaskImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.concurrent.Callable;

public interface Task extends Callable<DataObject> {

}
