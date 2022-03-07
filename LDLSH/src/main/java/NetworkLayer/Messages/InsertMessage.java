package NetworkLayer.Messages;

import NetworkLayer.Message;
import SystemLayer.Data.DataObject;

public record InsertMessage(
        DataObject dataObject
) implements Message {/**/}
