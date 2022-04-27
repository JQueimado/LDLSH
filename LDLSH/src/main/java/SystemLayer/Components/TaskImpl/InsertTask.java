package SystemLayer.Components.TaskImpl;

import NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.io.IOException;

public class InsertTask implements Task{

    private final Message insertRequest;

    private final DataContainer appContext;

    private final String hash_config;
    private final String erasure_config;
    private final String uid_config;
    private final int bands;

    public InsertTask( Message insertRequest, DataContainer appContext ) throws Exception {

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
    public DataObject call() {
        DataObject object = (DataObject) insertRequest.getBody();

        //PREPROCESS
        LSHHash object_hash = appContext.getLshHashFactory().getNewLSHHash(hash_config, appContext);
        object_hash.setObject(object, bands);

        ErasureCodes object_erasure_codes = appContext.getErasureCodesFactory().getNewErasureCodes(erasure_config);
        object_erasure_codes.encodeDataObject(object,bands);

        UniqueIdentifier object_unique_identifier = appContext.getUniqueIdentifierFactory().getNewUniqueIdentifier(uid_config);
        object_unique_identifier.setObject(object);

        //Package and Insert
        try {
            MultiMap[] multiMaps = appContext.getMultiMaps();
            for ( MultiMap multiMap : multiMaps ){
                multiMap.insert(object_hash, object_unique_identifier, object_erasure_codes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
