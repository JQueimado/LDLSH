package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Processes.PostProcessor;

public abstract class PostProcessorFactory implements Factory {

    public enum types {NONE,STANDARD}

    public static types current_type;

    public static void setCurrentType(String type) throws ConfigException {
        try{
            current_type = types.valueOf(type);
        }catch (Exception e){
            current_type = types.NONE;
            throw new ConfigException("Invalid config Type", "POST_PROCESSOR", type);
        }
    }

    public static PostProcessor getNewInstance(){
        //TODO
        return null;
    }
}
