package SystemLayer.Components.AdditionalStructures.StorageMap;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public abstract class StorageMapImpl implements StorageMap{

    protected DataContainer appContext;

    public StorageMapImpl(DataContainer appContext ){
        this.appContext = appContext;
    }

    @Override
    public abstract boolean insert(UniqueIdentifier key, DataObject<?> value) throws Exception;

    @Override
    public abstract DataObject<?> query(UniqueIdentifier key) throws Exception;
}
