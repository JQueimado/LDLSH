package NetworkLayer.Messages;

import SystemLayer.Data.ErasureCodes;
import SystemLayer.Data.ObjectName;

public record QueryResponseImpl(
        ErasureCodes erasureCodes,
        ObjectName objectName
) implements QueryResponse {
}
