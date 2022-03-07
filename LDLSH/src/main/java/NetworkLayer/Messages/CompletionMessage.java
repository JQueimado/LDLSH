package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.LocalitySensitiveHashing;
import SystemLayer.Data.ObjectName;

public record CompletionMessage(
        LocalitySensitiveHashing localitySensitiveHashing,
        ObjectName objectName
) implements Message {/**/}
