package SystemLayer.Components.DistanceMetricImpl;

import SystemLayer.Containers.DataContainer;
import info.debatty.java.lsh.MinHash;

import java.util.HashSet;
import java.util.Set;

public class JaccardDistanceMetric extends DistanceMetricImpl {

    public JaccardDistanceMetric(DataContainer context) {
        super(context);
    }

    protected Set<Integer> byteArrayToSet(byte[] intArray) {
        Set<Integer> intSet = new HashSet<>();
        for (int i : intArray)
            intSet.add(i);
        return intSet;
    }

    protected double jaccardDistance(Set<Integer> objectA, Set<Integer> objectB) {
        return 1 - MinHash.jaccardIndex(objectA, objectB);
    }

    @Override
    public double getDistance(byte[] object_a, byte[] object_b){
        return jaccardDistance( byteArrayToSet(object_a), byteArrayToSet(object_b) );
    }
}
