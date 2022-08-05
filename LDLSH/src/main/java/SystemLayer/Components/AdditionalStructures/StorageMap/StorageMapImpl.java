package SystemLayer.Components.AdditionalStructures.StorageMap;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public abstract class StorageMapImpl implements StorageMap{

    public StorageMapImpl(){

    }

    @Override
    public abstract void insert(UniqueIdentifier key, DataObject value);

    @Override
    public abstract DataObject query(UniqueIdentifier key);
}
