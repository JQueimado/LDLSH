package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.LocalitySensitiveHashing;

public record QueryMessageMultiBlock(
        LocalitySensitiveHashing localitySensitiveHashing
) implements Message {/**/}
