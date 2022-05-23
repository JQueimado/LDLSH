package Factories.DataFactories;

import Factories.Factory;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.UniqueIndentifierImpl.Sha256UniqueIdentifier;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class UniqueIdentifierFactory implements Factory {

    private final String config_name = "UNIQUE_IDENTIFIER";

    public enum configurations {NONE,SHA256}

    public UniqueIdentifier getNewUniqueIdentifier(DataContainer appContext){
        return getNewUniqueIdentifier( appContext.getConfigurator().getConfig(config_name) );
    }

    public UniqueIdentifier getNewUniqueIdentifier(String config){
        configurations type = configurations.valueOf(config);
        switch (type){

            case SHA256 -> {
                return new Sha256UniqueIdentifier();
            }

            default -> {
                return null;
            }
        }
    }
}
