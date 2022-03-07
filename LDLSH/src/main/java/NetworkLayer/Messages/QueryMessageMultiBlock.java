package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.LocalitySensitiveHashing;

public record QueryMessageComplete(
        LocalitySensitiveHashing localitySensitiveHashing
) implements Message {/**/}
