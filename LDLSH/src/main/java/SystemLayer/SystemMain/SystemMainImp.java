package SystemLayer.SystemMain;

import SystemLayer.Components.SystemServer.SystemImpl;
import SystemLayer.Components.SystemServer.SystemServer;
import SystemLayer.Containers.DataContainer;

public abstract class SystemMainImp implements SystemMain {

    protected String[] args;
    protected DataContainer appContext;
    protected SystemServer system;

    public SystemMainImp(String[] args, DataContainer appContext)throws Exception{
        this.args = args;
        this.appContext = appContext;
        this.system = new SystemImpl(appContext);
    }

    @Override
    public abstract void run() throws Exception;
}
