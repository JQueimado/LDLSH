package SystemLayer.Components.TaskImpl.Worker.Model;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.DataProcessor.DataProcessor;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.Worker.WorkerTaskImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataUnits.ModelMultimapValue;
import SystemLayer.SystemExceptions.InvalidMessageTypeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelInsertWorkerTask extends WorkerTaskImpl {

    public ModelInsertWorkerTask(Message insertRequest, DataContainer appContext ) throws Exception {
        super(insertRequest, appContext);
        if( insertRequest.getType() != Message.types.INSERT_REQUEST )
            throw new Exception("Invalid Message type for InsertTask");
    }

    @Override
    public DataObject call() throws Exception {
        if( message.getType() != Message.types.INSERT_REQUEST )
            throw new InvalidMessageTypeException(
                    Message.types.INSERT_REQUEST,
                    message.getType());

        DataObject object = (DataObject) message.getBody().get(0);

        //PREPROCESS
        DataProcessor.ProcessedData processedData = appContext.getDataProcessor().preProcessData(object);

        //Package and Insert
        try {
            MultiMap[] multiMaps = appContext.getMultiMaps();
            //Shuffle indexes
            List<Integer> indexes = new ArrayList<>();
            for ( int i=0; i<multiMaps.length; i++ ){
                indexes.add( i );
            }
            Collections.shuffle(indexes);

            //Insert
            for ( int i = 0; i<multiMaps.length; i++ ){
                MultiMap multiMap = multiMaps[i];

                ModelMultimapValue modelMultimapValue = new ModelMultimapValue(
                        processedData.object_lsh(),
                        processedData.object_uid(),
                        processedData.object_erasureCodes().getBlockAt(indexes.get(i))
                );

                multiMap.insert(
                        processedData.object_lsh(),
                        modelMultimapValue
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
