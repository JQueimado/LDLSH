package SystemLayer.ProcessInterfacesses;

import SystemLayer.Data.DataObject;
import SystemLayer.Data.ErasureCodes;

public interface ErasureProcessor {
    public ErasureCodes encoder(DataObject dataObject);
    public DataObject decoder(ErasureCodes erasureCodes);
}
