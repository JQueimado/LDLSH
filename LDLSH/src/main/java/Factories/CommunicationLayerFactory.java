package Factories;

import SystemLayer.Components.NetworkLayer.CommunicationLayer;
import SystemLayer.Components.NetworkLayer.NettyCommunicationLayerPackage.NettyCommunicationLayer;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class CommunicationLayerFactory extends FactoryImpl {

    private static final String communication_config = "COMMUNICATION_LAYER";
    private enum types {NONE, NETTY}

    public CommunicationLayerFactory(DataContainer appContext) {
        super(appContext);
    }

    public CommunicationLayer getNewCommunicationLayer(){
        try {
            String string_type = appContext.getConfigurator().getConfig(communication_config);
            return getNewCommunicationLayer(string_type);
        }catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
            return null;
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public CommunicationLayer getNewCommunicationLayer( String string_type ) throws Exception {
        try {
            types type = types.valueOf(string_type);
            switch (type) {

                case NETTY -> {
                    return new NettyCommunicationLayer(appContext);
                }

                default -> {
                    return null;
                }
            }
        }catch (IllegalArgumentException e){
            throw new UnknownConfigException(communication_config, string_type);
        }
    }

}
