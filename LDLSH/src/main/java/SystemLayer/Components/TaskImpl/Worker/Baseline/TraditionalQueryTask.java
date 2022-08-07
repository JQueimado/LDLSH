package SystemLayer.Components.TaskImpl.Worker.Baseline;

import Factories.ComponentFactories.AdditionalComponentsFactories.StorageMapFactory;
import SystemLayer.Components.AdditionalStructures.StorageMap.StorageMap;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.TaskImpl.Worker.WorkerTaskImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.DataUnits.ObjectMultimapValue;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.InvalidMessageTypeException;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TraditionalQueryTask extends TraditionalTask {

    public TraditionalQueryTask(Message insertMessage, DataContainer appContext) throws Exception{
        super(insertMessage, appContext);
        if( message.getType() != Message.types.QUERY_REQUEST )
            throw new InvalidMessageTypeException(Message.types.QUERY_REQUEST, message.getType());

    }

    @Override
    public DataObject<?> call() throws Exception {
        //Object
        DataObject<?> queryObject = (DataObject<?>) message.getBody().get(0);
        //LSH
        LSHHash objectHash = appContext.getDataProcessor().preprocessLSH( queryObject );

        try {
            Set<UniqueIdentifier> candidatesUid = new HashSet<>();

            //Query candidates Unique identifiers
            MultiMap[] multimaps = appContext.getMultiMaps();
            for (MultiMap currentMultimap : multimaps) {
                MultiMapValue[] results = currentMultimap.query(
                        objectHash.getBlockAt(currentMultimap.getHashBlockPosition())
                );

                for (MultiMapValue result : results) {
                    if (!(result instanceof ObjectMultimapValue))
                        throw new Exception("Invalid MultimapValue type.");
                    ObjectMultimapValue omv = (ObjectMultimapValue) result;
                    UniqueIdentifier uid = appContext.getUniqueIdentifierFactory().getNewUniqueIdentifier();
                    uid.setObject(omv.object());

                    candidatesUid.add(uid);
                }
            }

            if( candidatesUid.size() == 0 )
                return null;

            //Retrieve candidates from storage
            List<DataObject<?>> candidates = new ArrayList<>();
            StorageMap storageMap = getStorageMap();
            for (UniqueIdentifier candidateUid : candidatesUid){
                DataObject<?> candidate = storageMap.query(candidateUid);
                candidates.add(candidate);
            }

            if( candidates.size() == 1 )
                return candidates.get(0);

            //Compare Distances
            DataObject<?> bestCandidate = null;
            double mDistance = -1;
            for(DataObject<?> candidate : candidates){
                double distance = appContext.getDistanceMeasurer().getDistance(
                        queryObject.toByteArray(),
                        candidate.toByteArray()
                );

                if( distance < mDistance || bestCandidate == null ){
                    bestCandidate = candidate;
                    mDistance = distance;
                }
            }

            return bestCandidate;

        }catch (Exception e){
            e.printStackTrace();
        }

        return queryObject;
    }
}
