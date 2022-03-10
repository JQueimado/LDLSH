package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Processes.UniqueIdentifierProcessor;

public abstract class UniqueIdentifierProcessorFactory implements Factory {

    public enum types {NONE,STANDARD}

    public static types current_type;

    public static void setCurrentType(String type) throws ConfigException {
        try{
            current_type = types.valueOf(type);
        }catch (Exception e){
            current_type = types.NONE;
            throw new ConfigException("Invalid config Type", "UNIQUE_IDENTIFIER", type);
        }
    }

    public static UniqueIdentifierProcessor getNewInstance(){
        /*TODO*/
        return null;
    }
}
