package SystemLayer.Components.AdditionalStructures.StorageMap;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public interface StorageMap {

    public void insert(UniqueIdentifier key, DataObject<?> value) throws Exception;

    public DataObject<?> query(UniqueIdentifier key) throws Exception;

}
