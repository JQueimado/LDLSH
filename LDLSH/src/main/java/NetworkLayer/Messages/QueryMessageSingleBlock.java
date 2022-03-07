package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.LSHHash;

public record QueryMessageSingleBlock(
        LSHHash.LocalitySensitiveHashingBlock block
) implements Message {/**/}
