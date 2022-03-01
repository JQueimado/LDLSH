package NetworkLayer.Messages;

import SystemLayer.Data.DataObject;

public record InsertMessageImpl(
        DataObject dataObject
) implements InsertMessage {/**/}
