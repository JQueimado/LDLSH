package NetworkLayer.Messages;

import SystemLayer.Data.ErasureCodes;

public record CompletionResponseImpl(
        ErasureCodes erasureCodes
) implements CompletionResponse {/**/}
