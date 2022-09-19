package SystemLayer.Components.TaskImpl.Worker.Model;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.Worker.WorkerTaskImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataUnits.ModelMultimapValue;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.CorruptDataException;
import SystemLayer.SystemExceptions.IncompleteBlockException;
import SystemLayer.SystemExceptions.InvalidMapValueTypeException;
import SystemLayer.SystemExceptions.InvalidMessageTypeException;

import java.util.*;

public class ModelStandardQueryWorkerTask extends WorkerTaskImpl {

    public ModelStandardQueryWorkerTask(Message queryRequest, DataContainer appContext ) throws Exception {
        super(queryRequest, appContext);
        if( queryRequest.getType() != Message.types.QUERY_REQUEST )
            throw new Exception("Invalid Message type for QueryTask");
    }

    @Override
    public DataObject<?> call() throws Exception {

        if( message.getType() != Message.types.QUERY_REQUEST )
            throw new InvalidMessageTypeException( Message.types.QUERY_REQUEST, message.getType() );

        //Preprocess
        DataObject<?> queryObject = (DataObject<?>) message.getBody().get(0);
        LSHHash query_hash = appContext.getDataProcessor().preprocessLSH(queryObject);

        MultiMap[] multiMaps = appContext.getMultiMaps();
        List<MultiMapValue> results = new ArrayList<>();

        for (MultiMap multiMap : multiMaps) {
            MultiMapValue[] multimap_results = multiMap.query(query_hash.getBlockAt(multiMap.getHashBlockPosition()));
            if (multimap_results == null || multimap_results.length == 0)
                continue;
            Collections.addAll(results, multimap_results);
        }

        if( results.size() == 0 )
            return null;

        //Completion Grouping
        Map<UniqueIdentifier, Pair> objectMapping = new HashMap<>();
        //-group erasure codes
        for(MultiMapValue rawMultiMapValue: results){

            ModelMultimapValue multiMapValue;
            try{
                multiMapValue = (ModelMultimapValue) rawMultiMapValue;
            }catch (Exception e){
                throw new InvalidMapValueTypeException("Worker received invalid map value");
            }

            Pair pair = objectMapping.get( multiMapValue.uniqueIdentifier() );
            if( pair == null ){
                ErasureCodes temp_erasure_codes = appContext.getErasureCodesFactory().getNewErasureCodes();
                temp_erasure_codes.addBlockAt( multiMapValue.erasureCode() );

                pair = new Pair( temp_erasure_codes, multiMapValue.lshHash() );
                objectMapping.put(multiMapValue.uniqueIdentifier(), pair);

            }else {
                pair.erasureCodes.addBlockAt( multiMapValue.erasureCode() );
            }
        }

        //-Completion
        Set<DataObject<?>> potentialCandidates = new HashSet<>();
        for( UniqueIdentifier uid : objectMapping.keySet() ){
            ErasureCodes codes = objectMapping.get(uid).erasureCodes;

            //Attempt at decoding
            DataObject<?> temporaryObject;
            try{
                temporaryObject = appContext.getDataProcessor().postProcess(codes, uid);
            } catch (IncompleteBlockException ibe){
                //If decode fails by an incomplete block error, runs completion
                LSHHash objectHash = objectMapping.get( uid ).lshHash();

                //Complete
                for( MultiMap multiMap: multiMaps ){ //Go to all multiMaps and retrieve the intended erasure block
                    try {
                        ErasureCodesImpl.ErasureBlock block = multiMap.complete(objectHash, uid);
                        codes.addBlockAt(block);
                    }catch (Exception e){
                        //continue;
                    }
                }

                //Decode again
                try {
                    temporaryObject = appContext.getDataProcessor().postProcess(codes, uid);
                }catch (CorruptDataException | IncompleteBlockException e){
                    continue; //If an object is corrupt or incomplete after completion the candidate is ignored
                }
            }
            potentialCandidates.add( temporaryObject ); //Add candidate
        }

        //-Post Process
        DataObject<?> nearestNeighbour = null;
        double distance = -1;

        for( DataObject<?> potentialCandidate : potentialCandidates ){

            double c_distance = appContext.getDistanceMeasurer().getDistance(
                    potentialCandidate.toByteArray(),
                    queryObject.toByteArray() );

            if( c_distance < distance || distance == -1 ){
                nearestNeighbour = potentialCandidate;
                distance = c_distance;
            }
        }

        return nearestNeighbour;
    }

    //Pair
    private record Pair( ErasureCodes erasureCodes, LSHHash lshHash){

    }
}
