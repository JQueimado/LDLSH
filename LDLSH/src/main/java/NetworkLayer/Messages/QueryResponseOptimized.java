package NetworkLayer.Messages;

import SystemLayer.Data.ErasureCodes;
import SystemLayer.Data.ObjectName;

public record QueryResponseOptimizedImpl(
        ObjectName objectName
) implements QueryResponse {
}
