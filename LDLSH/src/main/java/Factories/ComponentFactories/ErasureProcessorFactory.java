package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Processes.ErasureProcessorImpl.ErasureProcessor;
import SystemLayer.Processes.ErasureProcessorImpl.ReedSolomonErasureCodesProcessor;

public abstract class ErasureProcessorFactory implements Factory {

    public enum types {NONE,REED_SOLOMON}

    public static types current_type;

    public static void setCurrentType(String type) throws ConfigException {
        try{
            current_type = types.valueOf(type);
        }catch (Exception e){
            current_type = types.NONE;
            throw new ConfigException("Invalid config Type", "ERASURE_PROCESSOR", type);
        }
    }

    public static ErasureProcessor getNewInstance(){

        switch (current_type){

            case REED_SOLOMON ->{
                    return new ReedSolomonErasureCodesProcessor();
            }

            default -> {return null;}
        }
    }
}
