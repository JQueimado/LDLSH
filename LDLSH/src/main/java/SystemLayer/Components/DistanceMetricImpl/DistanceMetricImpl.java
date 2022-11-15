package SystemLayer.Components.DistanceMetricImpl;

import SystemLayer.Containers.DataContainer;

import java.util.HashSet;
import java.util.Set;

public abstract class DistanceMetricImpl implements DistanceMetric {

    protected DataContainer context;

    public DistanceMetricImpl( DataContainer context ){
        this.context = context;
    }

    @Override
    public abstract double getDistance(byte[] object_a, byte[] object_b);
}
