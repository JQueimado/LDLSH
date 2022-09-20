package SystemLayer.Components.TaskImpl.Worker.Baseline;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.Worker.WorkerTaskImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataUnits.ObjectMultimapValue;
import SystemLayer.Data.LSHHashImpl.LSHHash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TraditionalReplicatedInsertTask extends WorkerTaskImpl {

    public TraditionalReplicatedInsertTask(Message insertMessage, DataContainer appContext) throws Exception{
        super(insertMessage, appContext);
        if( message.getType() != Message.types.INSERT_REQUEST )
            throw new Exception("Invalid Message type for InsertTask");

    }

    @Override
    public DataObject<?> call() throws Exception {

        DataObject<?> object = (DataObject<?>) message.getBody().get(0);
        LSHHash objectHash = appContext.getDataProcessor().preprocessLSH( object );

        try {
            List<MultiMap> multimaps = new ArrayList<>( appContext.getMultiMaps() );
            Collections.shuffle(multimaps);
            for ( MultiMap multiMap : multimaps ){
                multiMap.insert(objectHash, new ObjectMultimapValue( object.toByteArray() ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return object;
    }
}
