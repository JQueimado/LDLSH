package NetworkLayer.Messages;

import SystemLayer.Data.LocalitySensitiveHashing;
import SystemLayer.Data.ObjectName;

public record CompletionMessageImpl(
        LocalitySensitiveHashing localitySensitiveHashing,
        ObjectName objectName
) implements CompletionMessage{/**/}
