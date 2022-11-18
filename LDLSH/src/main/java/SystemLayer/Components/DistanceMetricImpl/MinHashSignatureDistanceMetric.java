package SystemLayer.Components.DistanceMetricImpl;

import SystemLayer.Containers.DataContainer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class MinHashSignatureDistanceMetric extends DistanceMetricImpl {

    public MinHashSignatureDistanceMetric(DataContainer context) {
        super(context);
    }

    @Override
    public double getDistance(byte[] object_a, byte[] object_b) {

        //Adapted form the info.debatty.java.lsh.MinHash hash similarity implementation;
        if (object_a.length != object_b.length) {
            return -1; //Null value
        }

        int sigSize = object_a.length/4;

        double sim = 0;
        for (int i = 0; i < object_a.length; i+=4) {

            //if all 4 bytes are the same then the int value for those bytes is the same
            if( object_a[i] != object_b[i] )
                continue;

            if( object_a[i+1] != object_b[i+1] )
                continue;

            if( object_a[i+2] != object_b[i+2] )
                continue;

            if( object_a[i+3] != object_b[i+3] )
                continue;

            sim += 1;
        }

        return 1 - ( sim / sigSize );
    }
}
