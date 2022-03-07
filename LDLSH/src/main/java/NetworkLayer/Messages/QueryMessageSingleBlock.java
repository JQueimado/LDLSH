package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.LocalitySensitiveHashing;

public record QueryMessageSingleBlock(
        LocalitySensitiveHashing.LocalitySensitiveHashingBlock block
) implements Message {/**/}
