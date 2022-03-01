package NetworkLayer.Messages;

import SystemLayer.Data.LocalitySensitiveHashing;

public record QueryMessageBlockImpl(
        LocalitySensitiveHashing.LocalitySensitiveHashingBlock block
) implements QueryMessage {/**/}
