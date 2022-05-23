package SystemLayer.Components.TaskImpl.Multimap;

import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.util.ArrayList;
import java.util.List;

public class CompletionMultimapTask implements MultimapTask<ErasureCodes.ErasureBlock[]> {

    private final DataContainer appContext;
    private final LSHHash object_hash;
    private final UniqueIdentifier uniqueIdentifier;

    public CompletionMultimapTask(
            LSHHash object_hash,
            UniqueIdentifier uniqueIdentifier,
            DataContainer appContext
    ){
        this.object_hash = object_hash;
        this.uniqueIdentifier = uniqueIdentifier;
        this.appContext = appContext;
    }

    @Override
    public ErasureCodes.ErasureBlock[] call() throws Exception {
        MultiMap[] multiMaps = appContext.getMultiMaps();
        List<ErasureCodes.ErasureBlock> results = new ArrayList<>();
        for ( MultiMap multiMap: multiMaps ){
            ErasureCodes.ErasureBlock block = multiMap.complete(object_hash, uniqueIdentifier);
            if (block != null)
                results.add(block);
        }
        return results.toArray(new ErasureCodes.ErasureBlock[0]);
    }
}
