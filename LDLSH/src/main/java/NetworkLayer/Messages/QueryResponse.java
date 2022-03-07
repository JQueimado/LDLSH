package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.ErasureCodes;
import SystemLayer.Data.ObjectName;

public record QueryResponse(
        ErasureCodes erasureCodes,
        ObjectName objectName
) implements Message {/**/}
