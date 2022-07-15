package SystemLayer.Components.TaskImpl.Worker;

import NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
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

public class ModelStandardQueryWorkerTask implements WorkerTask {

    private final Message queryRequest;

    private final DataContainer appContext;

    private final String dataObject_config;
    private final String hash_config;
    private final String erasure_config;
    private final int bands;

    public ModelStandardQueryWorkerTask(Message queryRequest, DataContainer appContext ) throws Exception {
        if( queryRequest.getType() != Message.types.QUERY_REQUEST )
            throw new Exception("Invalid Message type for QueryTask");

        this.queryRequest = queryRequest;
        this.appContext = appContext;
        this.dataObject_config = appContext.getConfigurator().getConfig("DATA_OBJECT");
        this.hash_config = appContext.getConfigurator().getConfig("LSH_HASH");
        this.erasure_config = appContext.getConfigurator().getConfig("ERASURE_CODES");
        this.bands = Integer.parseInt( appContext.getConfigurator().getConfig("N_BANDS") );

    }

    @Override
    public DataObject call() throws Exception {

        if( queryRequest.getType() != Message.types.QUERY_REQUEST )
            throw new InvalidMessageTypeException( Message.types.QUERY_REQUEST, queryRequest.getType() );

        //Preprocess
        DataObject queryObject = (DataObject) queryRequest.getBody().get(0);
        LSHHash query_hash = appContext.getDataProcessor().preprocessLSH(queryObject);

        MultiMap[] multiMaps = appContext.getMultiMaps();
        List<MultiMapValue> results = new ArrayList<>();

        for ( int i=0; i<multiMaps.length; i++ ){
            MultiMap multiMap = multiMaps[i];
            MultiMapValue[] multimap_results = multiMap.query( query_hash.getBlockAt( multiMap.getHashBlockPosition() ) );
            Collections.addAll(results, multimap_results);
        }

        if( results.size() == 0 )
            return null;

        //Completion Grouping
        Map<UniqueIdentifier, ErasureCodes> objectMapping = new HashMap<>();
        Map<UniqueIdentifier, LSHHash> hashMapping = new HashMap<>();
        //-group erasure codes
        for(MultiMapValue rawMultiMapValue: results){
            ModelMultimapValue multiMapValue;
            try{
                multiMapValue = (ModelMultimapValue) rawMultiMapValue;
            }catch (Exception e){
                throw new InvalidMapValueTypeException("Worker received invalid map value");
            }

            ErasureCodes erasureCodes = objectMapping.get( multiMapValue.uniqueIdentifier() );
            if( erasureCodes == null ){
                ErasureCodes temp_erasure_codes = appContext.getErasureCodesFactory()
                        .getNewErasureCodes(erasure_config);
                temp_erasure_codes.addBlockAt( multiMapValue.erasureCode() );
                objectMapping.put( multiMapValue.uniqueIdentifier(), temp_erasure_codes );
                hashMapping.put( multiMapValue.uniqueIdentifier(), multiMapValue.lshHash() );
            }else {
                erasureCodes.addBlockAt( multiMapValue.erasureCode() );
            }
        }

        //-Completion
        List<DataObject> potentialCandidates = new ArrayList<>();
        for( UniqueIdentifier uid : objectMapping.keySet() ){
            ErasureCodes codes = objectMapping.get(uid);

            //Attempt at decoding
            DataObject temporaryObject = null;
            try{
                temporaryObject = appContext.getDataProcessor().postProcess(codes, uid);

            } catch (IncompleteBlockException ibe){
                //If decode fails by an incomplete block error, runs completion TODO: OPTIMIZE COMPLETION
                LSHHash objectHash = hashMapping.get( uid );

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
        DataObject nearestNeighbour = potentialCandidates.get(0);
        double distance = appContext.getDistanceMeasurer().getDistance(
                nearestNeighbour.toByteArray(),
                queryObject.toByteArray() );

        for ( int i = 1; i<potentialCandidates.size(); i++ ){
            double c_distance = appContext.getDistanceMeasurer().getDistance(
                    potentialCandidates.get(i).toByteArray(),
                    queryObject.toByteArray() );

            if( c_distance < distance ){
                nearestNeighbour = potentialCandidates.get(i);
                distance = c_distance;
            }
        }

        return nearestNeighbour;
    }
}
