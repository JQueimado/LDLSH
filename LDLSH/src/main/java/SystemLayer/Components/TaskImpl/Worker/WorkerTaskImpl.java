package SystemLayer.Components.TaskImpl.Worker;

import NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

public abstract class WorkerTaskImpl implements WorkerTask{

    protected Message message;
    protected DataContainer appContext;

    public WorkerTaskImpl(Message message, DataContainer appContext){
        this.message = message;
        this.appContext = appContext;
    }

    @Override
    public abstract DataObject call() throws Exception;

}
