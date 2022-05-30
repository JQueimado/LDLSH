package Factories.DataFactories;

import Factories.Factory;
import Factories.FactoryImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;

public class DataObjectFactory extends FactoryImpl {

    private static final String config_name = "OBJECT_TYPE";

    private enum configurations {NONE,STRING}

    public DataObjectFactory(DataContainer appContext){
        super(appContext);
    }

    /**
     * Returns a new DataObject based on the configuration present on the appState
     * @return new DataObject
     */
    public DataObject getNewDataObject(){
        return getNewDataObject( appContext.getConfigurator().getConfig(config_name) );
    }

    /**
     * Returns a new DataObject based ona a given configuration
     * @param config_name object's configuration
     * @return new DataObject
     */
    public DataObject getNewDataObject(String config_name){

        configurations config = configurations.valueOf(config_name);

        switch (config){

            case STRING -> {
                return new StringDataObject();
            }

            default ->{
                return null;
            }
        }
    }
}
