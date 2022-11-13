package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Components.MultiMapImpl.GuavaInMemoryMultiMap;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.MultiMapImpl.RemoteMultimap;
import SystemLayer.Components.MultiMapImpl.SemiPersistentMultiMap;
import SystemLayer.Containers.DataContainer;


public class MultimapFactory implements Factory {

    private final String config_name = "MULTIMAP";

    private enum configurations {
        NONE,
        GUAVA_MEMORY_MULTIMAP,
        REMOTE_MULTIMAP,
        SEMI_PERSISTENT_MULTIMAP
    }

    private final DataContainer appContext;

    public MultimapFactory( DataContainer appContext ){
        this.appContext = appContext;
    }

    public MultiMap getNewMultiMap(){
        return getNewMultiMap( appContext.getConfigurator().getConfig(config_name) );
    }

    public MultiMap getNewMultiMap(String config){
        configurations configuration = configurations.valueOf(config);
        switch ( configuration ){

            case GUAVA_MEMORY_MULTIMAP -> {
                return new GuavaInMemoryMultiMap(appContext);
            }

            case REMOTE_MULTIMAP -> {
                return new RemoteMultimap(appContext);
            }

            case SEMI_PERSISTENT_MULTIMAP->{
                return new SemiPersistentMultiMap(appContext);
            }

            default ->{
                return null;
            }
        }
    }
}
