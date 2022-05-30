package SystemLayer.Components.TaskImpl.Multimap;

import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.concurrent.Callable;

public interface MultimapTask<T> extends Callable<T> {
}
