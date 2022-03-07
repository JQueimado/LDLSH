package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.ObjectName;

public record QueryResponseOptimized(
        ObjectName objectName
) implements Message {/**/}
