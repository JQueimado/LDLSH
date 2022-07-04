package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Components.SystemServer.SystemImpl;
import SystemLayer.Components.SystemServer.SystemServer;
import SystemLayer.Containers.DataContainer;

public class SystemServerFactory implements Factory {

    private enum configs { CENTRAL };

    public SystemServer newSystemServer(DataContainer dataContainer ) throws Exception{
        configs config = configs.valueOf( dataContainer.getConfigurator().getConfig("MODE") );

        switch (config){
            case CENTRAL -> {
                return new SystemImpl(dataContainer);
            }

            default -> {
                return null;
            }
        }
    }

}
