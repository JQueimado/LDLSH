package Factories.DataFactories;

import Factories.Factory;
import SystemLayer.Data.UniqueIndentifierImpl.Sha256UniqueIdentifier;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class UniqueIdentifierFactory implements Factory {
    public enum configurations {NONE,SHA256}

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
