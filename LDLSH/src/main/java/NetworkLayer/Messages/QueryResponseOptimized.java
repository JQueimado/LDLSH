package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.UniqueIdentifier;

public record QueryResponseOptimized(
        UniqueIdentifier objectName
) implements Message {/**/}
