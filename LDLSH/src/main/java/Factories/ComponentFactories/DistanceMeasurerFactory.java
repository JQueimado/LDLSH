package Factories.ComponentFactories;

import Factories.Factory;
import Factories.FactoryImpl;
import SystemLayer.Components.DistanceMetricImpl.DistanceMetric;
import SystemLayer.Components.DistanceMetricImpl.JaccardDistanceMetric;
import SystemLayer.Components.DistanceMetricImpl.NgramJaccardDistanceMetric;
import SystemLayer.Containers.DataContainer;

public class DistanceMeasurerFactory extends FactoryImpl {

    private enum configurations {
        NONE,
        JACCARD,
        NGRAM_JACCARD
    }

    public DistanceMeasurerFactory(DataContainer appContext){
        super(appContext);
    }

    public DistanceMetric getNewDistanceMeasurer(String config_name ){

        configurations config = configurations.valueOf(config_name);

        switch (config){

            case JACCARD -> {
                return new JaccardDistanceMetric(appContext);
            }

            case NGRAM_JACCARD -> {
                return new NgramJaccardDistanceMetric(appContext);
            }

            default ->{
                return null;
            }
        }
    }
}
