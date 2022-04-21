package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Components.MultiMapImpl.GuavaInMemoryMultiMap;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;


public class MultimapFactory implements Factory {
    private enum configurations {NONE,GUAVA_MEMORY_MULTIMAP}

    public MultimapFactory(){
        /**/
    }

    public MultiMap getNewMultiMap(String config, DataContainer dataContainer) throws Exception {
        configurations configuration = configurations.valueOf(config);
        switch ( configuration ){

            case GUAVA_MEMORY_MULTIMAP -> {
                return new GuavaInMemoryMultiMap(dataContainer);
            }

            default ->{
                return null;
            }
        }
    }
}
