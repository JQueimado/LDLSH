package SystemLayer.Components.TaskImpl.Multimap;

import NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.concurrent.Callable;

public interface MultimapTask extends Callable<Message> {
}
