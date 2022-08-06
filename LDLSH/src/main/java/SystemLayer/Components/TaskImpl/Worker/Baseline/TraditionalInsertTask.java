package SystemLayer.Components.TaskImpl.Worker.Baseline;

import Factories.ComponentFactories.AdditionalComponentsFactories.StorageMapFactory;
import SystemLayer.Components.AdditionalStructures.StorageMap.StorageMap;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.Worker.WorkerTaskImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataUnits.ObjectMultimapValue;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class TraditionalInsertTask extends WorkerTaskImpl {

    public TraditionalInsertTask(Message insertMessage, DataContainer appContext) throws Exception{
        super(insertMessage, appContext);
        if( message.getType() != Message.types.INSERT_REQUEST )
            throw new Exception("Invalid Message type for InsertTask");

    }

    private StorageMap getStorageMap(){
        Object[] additionalStructures = appContext.getAdditionalStructures();

        if( additionalStructures == null || additionalStructures[0] == null ){
            additionalStructures = new Object[1];
            StorageMapFactory storageMapFactory = new StorageMapFactory(appContext);
            additionalStructures[0] = storageMapFactory.getNewStorageMap();
        }

        return (StorageMap) additionalStructures[0];
    }

    @Override
    public DataObject<?> call() throws Exception {
        //Object
        DataObject<?> object = (DataObject<?>) message.getBody().get(0);
        //LSH
        LSHHash objectHash = appContext.getDataProcessor().preprocessLSH( object );
        //UID
        UniqueIdentifier uid = appContext.getUniqueIdentifierFactory().getNewUniqueIdentifier();
        uid.setObject( object.toByteArray() );


        try {
            MultiMap[] multimaps = appContext.getMultiMaps();
            for ( MultiMap multiMap : multimaps ){
                multiMap.insert(objectHash, new ObjectMultimapValue( uid.getUID() ));
            }

            //Storage map
            getStorageMap().insert(uid, object);

        }catch (Exception e){
            e.printStackTrace();
        }

        return object;
    }
}