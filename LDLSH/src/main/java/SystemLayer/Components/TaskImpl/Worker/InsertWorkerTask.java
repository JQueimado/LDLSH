package SystemLayer.Components.TaskImpl.Worker;

import NetworkLayer.Message;
import SystemLayer.Components.DataProcessor.DataProcessor;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class InsertWorkerTask implements WorkerTask {

    private final Message insertRequest;

    private final DataContainer appContext;

    private final String hash_config;
    private final String erasure_config;
    private final String uid_config;
    private final int bands;

    public InsertWorkerTask(Message insertRequest, DataContainer appContext ) throws Exception {

        if( insertRequest.getType() != Message.types.INSERT_REQUEST )
            throw new Exception("Invalid Message type for InsertTask");

        this.insertRequest = insertRequest;
        this.appContext = appContext;
        this.hash_config = appContext.getConfigurator().getConfig("LSH_HASH");
        this.erasure_config = appContext.getConfigurator().getConfig("ERASURE_CODES");
        this.uid_config = appContext.getConfigurator().getConfig("UNIQUE_IDENTIFIER");
        this.bands = Integer.parseInt( appContext.getConfigurator().getConfig("N_BANDS") );
    }

    @Override
    public DataObject call() throws Exception {
        DataObject object = (DataObject) insertRequest.getBody();

        //PREPROCESS
        DataProcessor.ProcessedData processedData = appContext.getDataProcessor().preProcessData(object);

        //Package and Insert
        try {
            MultiMap[] multiMaps = appContext.getMultiMaps();
            for ( MultiMap multiMap : multiMaps ){
                multiMap.insert(
                        processedData.object_lsh(),
                        processedData.object_uid(),
                        processedData.object_erasureCodes()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
