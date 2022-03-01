package NetworkLayer.Messages;

import SystemLayer.Data.LocalitySensitiveHashing;

public record QueryMessageCompleteImpl(
        LocalitySensitiveHashing localitySensitiveHashing
) implements QueryMessage {/**/}
