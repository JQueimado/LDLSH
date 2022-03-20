package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Processes.ErasureProcessorImpl.ErasureProcessor;
import SystemLayer.Processes.ErasureProcessorImpl.ReedSolomonErasureCodesProcessor;

public class ErasureProcessorFactory implements Factory {

    public enum configurations {NONE,REED_SOLOMON}

    //Constructors
    public ErasureProcessorFactory(){
        //
    }

    //getters
    public ErasureProcessor getNewProcessor( String config_name ){
        configurations config = configurations.valueOf(config_name);
        switch (config){

            case REED_SOLOMON ->{
                    return new ReedSolomonErasureCodesProcessor();
            }

            default -> {return null;}
        }
    }
}
