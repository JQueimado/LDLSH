package SystemLayer.Components.DistanceMeasurerImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import info.debatty.java.lsh.MinHash;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JaccardDistance implements DistanceMeasurer{

    private Set<Integer> toIntSet(byte[] array ){
        Set<Integer> set = new HashSet<>();
        int i;
        int j = array.length;
        for (i = 0; i<j; i++ ){
            int value = array[i];
            set.add( value );
        }
        return set;
    }

    @Override
    public double getDistance(byte[] object_a, byte[] object_b) {
        return 1 - MinHash.jaccardIndex( toIntSet(object_a), toIntSet(object_b) );
    }

    @Override
    public double getDistance(int[] object_a, int[] object_b) {
        return 1 - MinHash.jaccardIndex(
                Arrays.stream(object_a).boxed().collect(Collectors.toSet()),
                Arrays.stream(object_b).boxed().collect(Collectors.toSet())
        );
    }
}
