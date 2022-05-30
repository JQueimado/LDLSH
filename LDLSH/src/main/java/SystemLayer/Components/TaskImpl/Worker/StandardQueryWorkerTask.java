package SystemLayer.Components.TaskImpl.Worker;

import NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.IncompleteBlockException;

import java.util.*;

public class StandardQueryWorkerTask implements WorkerTask {

    private final Message queryRequest;

    private final DataContainer appContext;

    private final String dataObject_config;
    private final String hash_config;
    private final String erasure_config;
    private final int bands;

    public StandardQueryWorkerTask(Message queryRequest, DataContainer appContext ) throws Exception {
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
        //Preprocess
        DataObject queryObject = (DataObject) queryRequest.getBody();

        LSHHash query_hash = appContext.getLshHashFactory().getNewLSHHash();
        query_hash.setObject(queryObject.toByteArray(), bands);

        MultiMap[] multiMaps = appContext.getMultiMaps();
        List<MultiMap.MultiMapValue> results = new ArrayList<>();

        for ( MultiMap multiMap : multiMaps ){
            MultiMap.MultiMapValue[] multimap_results = multiMap.query( query_hash );
            Collections.addAll(results, multimap_results);
        }

        if( results.size() == 0 )
            return null;

        //Completion
        Map<UniqueIdentifier, ErasureCodes> objectMapping = new HashMap<>();
        Map<UniqueIdentifier, LSHHash> hashMapping = new HashMap<>();
        //-group erasure codes
        for(MultiMap.MultiMapValue multiMapValue: results){
            ErasureCodes erasureCodes = objectMapping.get( multiMapValue.uniqueIdentifier() );
            if( erasureCodes == null ){
                ErasureCodes temp_erasure_codes = appContext.getErasureCodesFactory()
                        .getNewErasureCodes(erasure_config);
                temp_erasure_codes.addBlockAt( multiMapValue.ErasureCode() );
                objectMapping.put( multiMapValue.uniqueIdentifier(), temp_erasure_codes );
                hashMapping.put( multiMapValue.uniqueIdentifier(), multiMapValue.lshHash() );
            }
        }

        //-Completion
        List<DataObject> potentialCandidates = new ArrayList<>();
        for( UniqueIdentifier uid : objectMapping.keySet() ){
            ErasureCodes codes = objectMapping.get(uid);
            DataObject temporaryObject = appContext.getDataObjectFactory().getNewDataObject(dataObject_config);

            //Attempt ar decoding
            try{
                byte[] tempData = codes.decodeDataObject();
                temporaryObject.setByteArray(tempData);
                potentialCandidates.add( temporaryObject );

            } catch (IncompleteBlockException ibe){
                //If decode fails by an incomplete block error, runs completion TODO: OPTIMIZE COMPLETION
                LSHHash objectHash = hashMapping.get( uid );

                //Complete
                for( MultiMap multiMap: multiMaps ){ //Go to all multiMaps and retrieve the intended erasure block
                    ErasureCodesImpl.ErasureBlock block = multiMap.complete(objectHash, uid);
                    codes.addBlockAt(block);
                }

                byte[] tempData = codes.decodeDataObject();
                temporaryObject.setByteArray(tempData);
                potentialCandidates.add( temporaryObject );

            }

        }

        //-Post Process
        DataObject nearestNeighbour = potentialCandidates.get(0);
        double distance = appContext.getDistanceMeasurer().getDistance( nearestNeighbour, queryObject );

        for ( int i = 1; i<potentialCandidates.size(); i++ ){
            double c_distance = appContext.getDistanceMeasurer().getDistance( potentialCandidates.get(i), queryObject );

            if( c_distance < distance ){
                nearestNeighbour = potentialCandidates.get(i);
                distance = c_distance;
            }
        }

        return nearestNeighbour;
    }
}
