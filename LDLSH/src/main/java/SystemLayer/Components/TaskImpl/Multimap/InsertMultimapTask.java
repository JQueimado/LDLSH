package SystemLayer.Components.TaskImpl.Multimap;

import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class InsertMultimapTask implements MultimapTask<Boolean> {

    private final LSHHash object_hash;
    private final UniqueIdentifier object_unique_identifier;
    private final ErasureCodes object_erasure_codes;
    private final DataContainer appContext;

    public InsertMultimapTask(
            LSHHash object_hash,
            UniqueIdentifier object_unique_identifier,
            ErasureCodes object_erasure_codes,
            DataContainer appContext)
    {
        this.appContext = appContext;
        this.object_hash = object_hash;
        this.object_unique_identifier = object_unique_identifier;
        this.object_erasure_codes = object_erasure_codes;
    }

    @Override
    public Boolean call() {
        try {
            MultiMap[] multiMaps = appContext.getMultiMaps();
            for ( MultiMap multiMap : multiMaps ){
                multiMap.insert(object_hash, object_unique_identifier, object_erasure_codes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
