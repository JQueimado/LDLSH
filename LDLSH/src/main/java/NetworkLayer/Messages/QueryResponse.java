package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.ErasureCodes;
import SystemLayer.Data.UniqueIdentifier;

public record QueryResponse(
        ErasureCodes erasureCodes,
        UniqueIdentifier objectName
) implements Message {/**/}
