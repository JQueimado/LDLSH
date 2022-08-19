package SystemLayer.Components.TaskImpl.TraditionalAux;

import Factories.ComponentFactories.AdditionalComponentsFactories.StorageMapFactory;
import SystemLayer.Components.AdditionalStructures.StorageMap.StorageMap;
import SystemLayer.Containers.DataContainer;

public class TraditionalAux {

    public synchronized static StorageMap getStorageMap(DataContainer appContext){
        Object[] additionalStructures = appContext.getAdditionalStructures();

        if( additionalStructures == null || additionalStructures[0] == null ){
            additionalStructures = new Object[1];
            StorageMapFactory storageMapFactory = new StorageMapFactory(appContext);
            additionalStructures[0] = storageMapFactory.getNewStorageMap();
            appContext.setAdditionalStructures(additionalStructures);
        }

        return (StorageMap) additionalStructures[0];
    }

}
