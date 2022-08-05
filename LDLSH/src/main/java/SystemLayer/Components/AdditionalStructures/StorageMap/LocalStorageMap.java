package SystemLayer.Components.AdditionalStructures.StorageMap;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.util.HashMap;
import java.util.Map;

public class LocalStorageMap extends StorageMapImpl{

    private Map<UniqueIdentifier, DataObject> map;

    public LocalStorageMap(){
        super();
        map = new HashMap<>();
    }

    @Override
    public void insert(UniqueIdentifier key, DataObject value) {
        map.put(key, value);
    }

    @Override
    public DataObject query(UniqueIdentifier key) {
        return map.get(key);
    }
}
