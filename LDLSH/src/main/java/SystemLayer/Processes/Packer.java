package SystemLayer.Processes;

import SystemLayer.Data.ErasureCodes;
import SystemLayer.Data.LocalitySensitiveHashing;
import SystemLayer.Data.ObjectName;

public interface Packer {
    public Package[] pack(ObjectName objectName, LocalitySensitiveHashing lsh, ErasureCodes codes);
}
