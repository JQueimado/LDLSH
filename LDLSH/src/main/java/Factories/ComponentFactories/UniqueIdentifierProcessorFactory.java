package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Processes.UniqueIdentifierProcessorImpl.Sha256Procesor;
import SystemLayer.Processes.UniqueIdentifierProcessorImpl.UniqueIdentifierProcessor;

public class UniqueIdentifierProcessorFactory implements Factory {

    public enum configurations {NONE,SHA256}

    public UniqueIdentifierProcessor getUniqueIdentifierProcessor(configurations config){
        switch (config){

            case SHA256 -> {
                return new Sha256Procesor();
            }

            default -> {
                return null;
            }
        }
    }
}
