package SystemLayer.Components.DistanceMeasurerImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import info.debatty.java.lsh.MinHash;

import java.util.HashSet;
import java.util.Set;

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
    public double getDistance(DataObject object_a, DataObject object_b) {
        return 1 - MinHash.jaccardIndex( toIntSet(object_a.toByteArray()), toIntSet(object_b.toByteArray()) );
    }
}
