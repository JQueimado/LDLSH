package SystemLayer.Components.AdditionalStructures.StorageMap;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalStorageMap extends StorageMapImpl{

    private final ConcurrentHashMap<UniqueIdentifier, DataObject<?>> map;

    public LocalStorageMap(DataContainer appContext){
        super(appContext);
        map = new ConcurrentHashMap<>();
    }

    @Override
    public void insert(UniqueIdentifier key, DataObject<?> value) {
        map.put(key, value);

        /* DEBUG */
        System.out.println(map.size());
    }

    @Override
    public DataObject<?> query(UniqueIdentifier key) {
        return map.get(key);
    }
}
