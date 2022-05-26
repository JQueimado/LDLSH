package SystemLayer.Components.DataProcessor;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public interface DataProcessor {
    ProcessedData preProcessData(DataObject object);
    DataObject postProcess();

    record ProcessedData(
            DataObject object,
            LSHHash object_lsh,
            UniqueIdentifier object_uid,
            ErasureCodes object_erasureCodes
    ){}
}
