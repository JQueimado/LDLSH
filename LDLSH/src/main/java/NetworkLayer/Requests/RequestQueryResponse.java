package NetworkLayer.Requests;

import NetworkLayer.Message;
import NetworkLayer.Request;
import SystemLayer.Data.DataObject;

public record RequestQueryResponse(DataObject dataObject) implements Request {
}
