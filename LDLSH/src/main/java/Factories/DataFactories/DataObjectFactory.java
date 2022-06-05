package Factories.DataFactories;

import Factories.Factory;
import Factories.FactoryImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.ByteArrayDataObject;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class DataObjectFactory extends FactoryImpl {

    private static final String config_name = "OBJECT_TYPE";

    private enum configurations {NONE, STRING, BYTE_ARRAY}

    public DataObjectFactory(DataContainer appContext){
        super(appContext);
    }

    /**
     * Returns a new DataObject based on the configuration present on the appState
     * @return new DataObject
     */
    public DataObject getNewDataObject(){
        try {
            return getNewDataObject(appContext.getConfigurator().getConfig(config_name));
        }catch (UnknownConfigException e){
            UnknownConfigException.handler(e);
            return null;
        }
    }

    /**
     * Returns a new DataObject based ona a given configuration
     * @param config_setting object's configuration
     * @return new DataObject
     */
    public DataObject getNewDataObject(String config_setting) throws UnknownConfigException {
        try {
            configurations config = configurations.valueOf(config_setting);

            switch (config) {

                case STRING -> {
                    return new StringDataObject();
                }

                case BYTE_ARRAY -> {
                    return new ByteArrayDataObject();
                }

                default -> {
                    return null;
                }
            }
        }catch (IllegalArgumentException e){
            throw new UnknownConfigException(config_name, config_setting);
        }
    }
}
