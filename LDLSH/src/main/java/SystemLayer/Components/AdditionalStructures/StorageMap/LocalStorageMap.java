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
    public boolean insert(UniqueIdentifier key, DataObject<?> value) {
        DataObject<?> rv = map.put(key, value);
        return rv == null;
    }

    @Override
    public DataObject<?> query(UniqueIdentifier key) {
        return map.get(key);
    }
}
