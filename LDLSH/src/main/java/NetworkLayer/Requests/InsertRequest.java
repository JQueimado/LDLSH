package NetworkLayer.Requests;

import NetworkLayer.Messages.Message;
import SystemLayer.Data.DataObject;

public interface InsertRequest extends Message {
    DataObject getDataObject();
    void setDataObject(DataObject dataObject);
}
