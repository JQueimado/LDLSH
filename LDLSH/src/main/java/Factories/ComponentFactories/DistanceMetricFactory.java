package Factories.ComponentFactories;

import Factories.FactoryImpl;
import SystemLayer.Components.DistanceMetricImpl.DistanceMetric;
import SystemLayer.Components.DistanceMetricImpl.JaccardDistanceMetric;
import SystemLayer.Components.DistanceMetricImpl.MinHashSignatureDistanceMetric;
import SystemLayer.Components.DistanceMetricImpl.NgramJaccardDistanceMetric;
import SystemLayer.Containers.DataContainer;

public class DistanceMetricFactory extends FactoryImpl {

    private enum configurations {
        NONE,
        JACCARD,
        NGRAM_JACCARD,
        MINHASH_SIGNATURE
    }

    public DistanceMetricFactory(DataContainer appContext){
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

            case MINHASH_SIGNATURE -> {
                return new MinHashSignatureDistanceMetric(appContext);
            }

            default ->{
                return null;
            }
        }
    }
}
