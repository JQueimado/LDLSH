package SystemLayer.Components.TaskImpl.Worker;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

public abstract class WorkerTaskImpl implements WorkerTask{

    protected Message message;
    protected DataContainer appContext;

    public WorkerTaskImpl(Message message, DataContainer appContext){
        this.message = message;
        this.appContext = appContext;
    }

    protected int[] objectsToIntArray( byte[] object ){
        int[] intArray = new int[object.length];
        for( int i = 0; i<object.length; i++ ){
            intArray[i] = object[i];
        }
        return intArray;
    }

    @Override
    public abstract DataObject<?> call() throws Exception;

}
