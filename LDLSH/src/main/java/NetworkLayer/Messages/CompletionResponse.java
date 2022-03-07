package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.ErasureCodes;

public record CompletionResponse(
        ErasureCodes erasureCodes
) implements Message {/**/}
