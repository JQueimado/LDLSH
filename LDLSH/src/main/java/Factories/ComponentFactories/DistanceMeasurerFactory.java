package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Processes.DistanceMeasurer;

public abstract class DistanceMeasurerFactory implements Factory {

    public enum types {NONE,STANDARD}

    public static types current_type;

    public static void setCurrentType(String type) throws ConfigException {
        //Blank type
        if (type.isEmpty() || type.isBlank()){
            current_type = types.NONE;
            return;
        }

        try{
            current_type = types.valueOf(type);
        }catch (IllegalArgumentException | NullPointerException iae){
            current_type = types.NONE;
            throw new ConfigException("Invalid config Type", "DISTANCE_MEASURER", type);
        }
    }

    public static DistanceMeasurer getNewDistanceMeasurer(){
        //TODO
        return null;
    }
}
