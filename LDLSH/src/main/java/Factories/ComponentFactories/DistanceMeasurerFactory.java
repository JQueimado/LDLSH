package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Processes.DistanceMeasurerImpl.DistanceMeasurer;
import SystemLayer.Processes.DistanceMeasurerImpl.JaccardDistance;

public class DistanceMeasurerFactory implements Factory {

    private enum configurations {NONE,JACCARD}

    public DistanceMeasurerFactory(){
        //
    }

    public DistanceMeasurer getNewDistanceMeasurer( String config_name ){

        configurations config = configurations.valueOf(config_name);

        switch (config){

            case JACCARD -> {
                return new JaccardDistance();
            }

            default ->{
                return null;
            }
        }
    }
}
