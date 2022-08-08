package Factories.ComponentFactories.AdditionalComponentsFactories;

import Factories.FactoryImpl;
import SystemLayer.Components.AdditionalStructures.StorageMap.LocalStorageMap;
import SystemLayer.Components.AdditionalStructures.StorageMap.RemoteStorageMap;
import SystemLayer.Components.AdditionalStructures.StorageMap.StorageMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class StorageMapFactory extends FactoryImpl {

    private final String storageMap_config = "STORAGE_MAP";

    private enum configurations {NONE,LOCAL,REMOTE}

    public StorageMapFactory(DataContainer dataContainer){
        super(dataContainer);
    }

    public StorageMap getNewStorageMap(){
        try{
            String config = appContext.getConfigurator().getConfig( storageMap_config );
            return getNewStorageMap(config);
        }catch (UnknownConfigException e){
            UnknownConfigException.handler( e );
            return null;
        }
    }

    public StorageMap getNewStorageMap(String config_name ) throws UnknownConfigException {
        try {
            configurations config = configurations.valueOf(config_name);

            switch (config) {

                case LOCAL -> {
                    return new LocalStorageMap(appContext);
                }

                case REMOTE -> {
                    return new RemoteStorageMap(appContext);
                }

                default -> {
                    return null;
                }
            }
        }catch (IllegalArgumentException e){
            throw new UnknownConfigException( storageMap_config, config_name );
        }
    }
}

