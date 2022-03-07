package NetworkLayer.Messages;

import SystemLayer.Data.LocalitySensitiveHashing;
import SystemLayer.Data.ObjectName;

public record CompletionMessage(
        LocalitySensitiveHashing localitySensitiveHashing,
        ObjectName objectName
) implements Message {
    public static CompletionMessage emptyRecord() {
        return new CompletionMessage(null, null);
    }
}
