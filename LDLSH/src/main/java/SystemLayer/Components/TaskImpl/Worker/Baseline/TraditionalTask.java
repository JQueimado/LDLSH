package SystemLayer.Components.TaskImpl.Worker.Baseline;

import Factories.ComponentFactories.AdditionalComponentsFactories.StorageMapFactory;
import SystemLayer.Components.AdditionalStructures.StorageMap.StorageMap;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.TaskImpl.Worker.WorkerTaskImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

public abstract class TraditionalTask extends WorkerTaskImpl {

    public TraditionalTask(Message message, DataContainer appContext) {
        super(message, appContext);
    }

    protected StorageMap getStorageMap(){
        Object[] additionalStructures = appContext.getAdditionalStructures();

        if( additionalStructures == null || additionalStructures[0] == null ){
            additionalStructures = new Object[1];
            StorageMapFactory storageMapFactory = new StorageMapFactory(appContext);
            additionalStructures[0] = storageMapFactory.getNewStorageMap();
        }

        return (StorageMap) additionalStructures[0];
    }

    @Override
    public abstract DataObject call() throws Exception;
}
