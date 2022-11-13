package SystemLayer.Components.DistanceMeasurerImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import info.debatty.java.lsh.MinHash;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JaccardDistance implements DistanceMeasurer{

    private Set<Integer> intArrayToSet( int[] intArray ){
       Set<Integer> intSet = new HashSet<>();
       for ( int i : intArray )
           intSet.add(i);
       return intSet;
    }

    @Override
    public double getDistance(int[] object_a, int[] object_b) {
        return 1 - MinHash.jaccardIndex( intArrayToSet(object_a), intArrayToSet(object_b) );
    }
}
