package SystemLayer.Components.TaskImpl.Worker.Baseline;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.Worker.WorkerTaskImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataUnits.ObjectMultimapValue;
import SystemLayer.Data.LSHHashImpl.LSHHash;

public class TraditionalReplicatedInsertTask extends WorkerTaskImpl {

    public TraditionalReplicatedInsertTask(Message insertMessage, DataContainer appContext) throws Exception{
        super(insertMessage, appContext);
        if( message.getType() != Message.types.INSERT_REQUEST )
            throw new Exception("Invalid Message type for InsertTask");

    }

    @Override
    public DataObject call() throws Exception {

        DataObject object = (DataObject) message.getBody().get(0);
        LSHHash objectHash = appContext.getDataProcessor().preprocessLSH( object );

        try {
            MultiMap[] multimaps = appContext.getMultiMaps();
            for ( MultiMap multiMap : multimaps ){
                multiMap.insert(objectHash, new ObjectMultimapValue( object.toByteArray() ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return object;
    }
}
