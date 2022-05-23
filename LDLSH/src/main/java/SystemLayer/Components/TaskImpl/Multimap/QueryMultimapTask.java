package SystemLayer.Components.TaskImpl.Multimap;

import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.LSHHashImpl.LSHHash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QueryMultimapTask implements MultimapTask<MultiMap.MultiMapValue[]> {

    private DataContainer appContext;
    private LSHHash object_hash;

    public QueryMultimapTask( LSHHash object_hash, DataContainer appContext){
        this.object_hash = object_hash;
        this.appContext = appContext;
    }

    @Override
    public MultiMap.MultiMapValue[] call() throws Exception {
        MultiMap[] multiMaps = appContext.getMultiMaps();
        List<MultiMap.MultiMapValue> results = new ArrayList<>();
        for( MultiMap multiMap : multiMaps ) {
            MultiMap.MultiMapValue[] temp = multiMap.query(object_hash);
            Collections.addAll(results, temp);
        }
        return results.toArray(new MultiMap.MultiMapValue[0]);
    }
}
