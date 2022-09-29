package SystemLayer.Components.TaskImpl.Worker.Baseline;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.Worker.WorkerTaskImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.DataUnits.ObjectMultimapValue;
import SystemLayer.Data.LSHHashImpl.LSHHash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TraditionalReplicatedQueryTask extends WorkerTaskImpl {

    public TraditionalReplicatedQueryTask(Message queryMessage, DataContainer appContext) throws Exception{
        super(queryMessage, appContext);
        if( message.getType() != Message.types.QUERY_REQUEST )
            throw new Exception("Invalid Message type for InsertTask");

    }

    @Override
    public DataObject<?> call() throws Exception {

        //Preprocess
        DataObject<?> queryObject = (DataObject<?>) message.getBody().get(0);
        LSHHash queryHash = appContext.getDataProcessor().preprocessLSH( queryObject );

        List<MultiMapValue> multiMapValues = new ArrayList<>();

        try {
            //Query values
            List<MultiMap> multimaps = new ArrayList<>( appContext.getMultiMaps() );
            Collections.shuffle(multimaps);
            for (MultiMap multiMap : multimaps) {
                Collections.addAll(
                        multiMapValues,
                        multiMap.query(queryHash.getBlockAt(multiMap.getHashBlockPosition()))
                );
            }

            if( multiMapValues.size() == 0 )
                return null;

            //Compare
            double mDistance = -1;
            byte[] result = null;
            for (MultiMapValue mapValue : multiMapValues){
                ObjectMultimapValue objectMultimapValue = (ObjectMultimapValue) mapValue;
                byte[] candidate = objectMultimapValue.object();

                double cDistance = appContext.getDistanceMeasurer().getDistance( queryObject.toByteArray(), candidate );

                if( result == null || cDistance < mDistance ){
                    result = candidate;
                    mDistance = cDistance;
                }

            }

            //Post process
            DataObject<?> dataObject = appContext.getDataObjectFactory().getNewDataObject();
            dataObject.setByteArray( result );
            return dataObject;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
