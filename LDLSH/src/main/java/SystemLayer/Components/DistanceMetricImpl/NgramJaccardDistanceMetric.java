package SystemLayer.Components.DistanceMetricImpl;

import SystemLayer.Components.AdditionalStructures.AuxiliarImplementations.NgramProcessor;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;
import info.debatty.java.lsh.MinHash;

import java.util.HashSet;
import java.util.Set;

public class NgramJaccardDistanceMetric extends JaccardDistanceMetric {

    public NgramJaccardDistanceMetric(DataContainer context) {
        super(context);
    }

    @Override
    public double getDistance(byte[] object_a, byte[] object_b) {
        Set<Integer> objectANgrams;
        Set<Integer> objectBNgrams;
        try {
            objectANgrams = NgramProcessor.create_ngrams(object_a, context);
            objectBNgrams = NgramProcessor.create_ngrams(object_b, context);
        }catch (UnknownConfigException unknownConfigException){
            UnknownConfigException.handler( unknownConfigException );
            return -1;
        }
        return jaccardDistance( objectANgrams, objectBNgrams );
    }
}
