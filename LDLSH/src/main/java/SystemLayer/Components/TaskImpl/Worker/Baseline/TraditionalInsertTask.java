package SystemLayer.Components.TaskImpl.Worker.Baseline;

import Factories.ComponentFactories.AdditionalComponentsFactories.StorageMapFactory;
import SystemLayer.Components.AdditionalStructures.StorageMap.StorageMap;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.TraditionalAux.TraditionalAux;
import SystemLayer.Components.TaskImpl.Worker.WorkerTaskImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataUnits.ObjectMultimapValue;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TraditionalInsertTask extends WorkerTaskImpl {

    StorageMap storageMap;

    public TraditionalInsertTask(Message insertMessage, DataContainer appContext) throws Exception{
        super(insertMessage, appContext);
        if( message.getType() != Message.types.INSERT_REQUEST )
            throw new Exception("Invalid Message type for InsertTask");

        storageMap = TraditionalAux.getStorageMap(appContext);
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
            List<MultiMap> multimaps = Arrays.asList(appContext.getMultiMaps());
            Collections.shuffle(multimaps);
            for ( MultiMap multiMap : multimaps ){
                multiMap.insert(objectHash, new ObjectMultimapValue( uid.getUID() ));
            }

            //Storage map
            storageMap.insert(uid, object);

        }catch (Exception e){
            e.printStackTrace();
        }

        return object;
    }
}
