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
import SystemLayer.SystemExceptions.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.*;

public class ModelOptimizedQueryWorkerTask extends WorkerTaskImpl {

    public ModelOptimizedQueryWorkerTask(Message queryRequest, DataContainer appContext ) throws Exception {
        super(queryRequest, appContext);
        if( queryRequest.getType() != Message.types.QUERY_REQUEST )
            throw new Exception("Invalid Message type for QueryTask");
    }

    private int[] signatureToIntArray( byte[] signature ){
        ByteBuffer byteBuffer = ByteBuffer.wrap(signature).order(ByteOrder.BIG_ENDIAN);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        int[] intArray = new int[intBuffer.remaining()];
        intBuffer.get(intArray);
        return intArray;
    }

    @Override
    public DataObject<?> call() throws Exception {

        if( message.getType() != Message.types.QUERY_REQUEST )
            throw new InvalidMessageTypeException( Message.types.QUERY_REQUEST, message.getType() );

        //Preprocess
        DataObject<?> queryObject = (DataObject<?>) message.getBody().get(0);
        LSHHash query_hash = appContext.getDataProcessor().preprocessLSH(queryObject);

        List<MultiMap> multiMaps = new ArrayList<>( appContext.getMultiMaps() );
        Collections.shuffle(multiMaps);
        Map<UniqueIdentifier, Pair> objectMapping = new HashMap<>();

        //Query
        for (MultiMap multiMap : multiMaps) {
            MultiMapValue[] multimap_results = multiMap.query(query_hash.getBlockAt(multiMap.getHashBlockPosition()));
            if (multimap_results == null || multimap_results.length == 0)
                continue;

            for(MultiMapValue rawMultiMapValue : multimap_results){
                ModelMultimapValue multimapValue;
                try{
                    multimapValue = (ModelMultimapValue) rawMultiMapValue;
                }catch (Exception e){
                    throw new InvalidMapValueTypeException("Worker received invalid map value");
                }

                Pair object = objectMapping.get( multimapValue.uniqueIdentifier() );
                if( object == null ){
                    ErasureCodes temp_erasure_codes = appContext.getErasureCodesFactory().getNewErasureCodes();
                    temp_erasure_codes.addBlockAt( multimapValue.erasureCode() );

                    object = new Pair( temp_erasure_codes, multimapValue.lshHash());

                    objectMapping.put(multimapValue.uniqueIdentifier(), object);

                }else {
                    object.erasureCodes.addBlockAt( multimapValue.erasureCode() );
                }
            }
        }

        if( objectMapping.size() == 0 )
            return null;

        //Evaluate
        UniqueIdentifier bestCandidateUID = null;
        double distance = -1;

        for( UniqueIdentifier currentUid: objectMapping.keySet() ){
            LSHHash currentHash = objectMapping.get(currentUid).lshHash();
            double currentDistance = appContext.getDistanceMeasurer().getDistance(
                    currentHash.getSignature(),
                    query_hash.getSignature()
            );

            if( distance == -1 || currentDistance < distance ){
                distance = currentDistance;
                bestCandidateUID = currentUid;
            }
        }

        //Completion and postprocessor
        Pair pair = objectMapping.get(bestCandidateUID);
        ErasureCodes bestCandidateErasureCodes = pair.erasureCodes();
        LSHHash bestCandidateLSH = pair.lshHash();
        DataObject<?> bestCandidate;

        try{
            bestCandidate = appContext.getDataProcessor().postProcess(bestCandidateErasureCodes, bestCandidateUID);
        }catch (IncompleteBlockException ibe){
            //If decode fails by an incomplete block error, runs completion

            //Complete
            for( MultiMap multiMap: multiMaps ){ //Go to all multiMaps and retrieve the intended erasure block
                try {
                    ErasureCodesImpl.ErasureBlock block = multiMap.complete(bestCandidateLSH, bestCandidateUID);
                    bestCandidateErasureCodes.addBlockAt(block);
                }catch (Exception e){
                    //continue;
                }
            }

            //Decode again
            try {
                bestCandidate = appContext.getDataProcessor().postProcess(bestCandidateErasureCodes, bestCandidateUID);
            }catch (CorruptDataException | IncompleteBlockException e){
                return null;
            }
        }

        return bestCandidate;
    }

    //Pair
    private record Pair( ErasureCodes erasureCodes, LSHHash lshHash){

    }
}
