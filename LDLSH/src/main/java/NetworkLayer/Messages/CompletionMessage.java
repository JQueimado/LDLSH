package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.LSHHash;
import SystemLayer.Data.UniqueIdentifier;

public record CompletionMessage(
        LSHHash localitySensitiveHashing,
        UniqueIdentifier objectName
) implements Message {/**/}
