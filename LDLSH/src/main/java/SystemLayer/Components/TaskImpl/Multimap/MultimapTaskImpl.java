package SystemLayer.Components.TaskImpl.Multimap;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;

public abstract class MultimapTaskImpl implements MultimapTask{

    protected Message message;
    protected DataContainer appContext;

    public MultimapTaskImpl( Message message, DataContainer appContext ){
        this.message = message;
        this.appContext = appContext;
    }

    @Override
    public abstract Message call() throws Exception;
}
