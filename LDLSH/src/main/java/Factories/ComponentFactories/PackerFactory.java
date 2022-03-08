package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.ProcessInterfacesses.Packer;

public abstract class PackerFactory implements Factory {

    public enum types {NONE,STANDARD}

    public static types current_type;

    public static void setCurrentType(String type) throws ConfigException{
        try{
            current_type = types.valueOf(type);
        }catch (Exception e){
            current_type = types.NONE;
            throw new ConfigException("Invalid config Type", "PACKER_FACTORY", type);
        }
    }

    public static Packer getNewInstance(){
        //TODO
        return null;
    }
}
