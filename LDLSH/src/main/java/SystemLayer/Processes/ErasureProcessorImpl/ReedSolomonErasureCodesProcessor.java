package SystemLayer.Processes.ErasureProcessorImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodes;

public class ReedSolomonErasureCodesProcessor implements ErasureProcessor{
    @Override
    public ErasureCodes encoder(DataObject dataObject) {
        return null;
    }

    @Override
    public DataObject decoder(ErasureCodes erasureCodes) {
        return null;
    }
}
