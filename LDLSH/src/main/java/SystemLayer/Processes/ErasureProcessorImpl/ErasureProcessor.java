package SystemLayer.Processes.ErasureProcessorImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodes;

public interface ErasureProcessor {
    public ErasureCodes encoder(DataObject dataObject);
    public DataObject decoder(ErasureCodes erasureCodes);
}
