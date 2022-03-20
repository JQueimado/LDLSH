package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Processes.LSHHashFactory.LSHHashProcessor;
import SystemLayer.Processes.LSHHashFactory.JavaMinHash;

public class LSHHashProcessorFactory implements Factory {

    public enum configurations {NONE,MINHASH}

    public LSHHashProcessor getNewInstance( configurations config, DataContainer dataContainer ) throws Exception {
        switch (config){

            case MINHASH -> {
                return new JavaMinHash( dataContainer );
            }

            default -> {
                return null;
            }
        }
    }
}
