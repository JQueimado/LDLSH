package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.LSHHash;

public record QueryMessageMultiBlock(
        LSHHash localitySensitiveHashing
) implements Message {/**/}
