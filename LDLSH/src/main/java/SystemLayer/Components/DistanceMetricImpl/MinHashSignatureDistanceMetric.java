package SystemLayer.Components.DistanceMetricImpl;

import SystemLayer.Containers.DataContainer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class MinHashSignatureDistanceMetric extends DistanceMetricImpl {

    public MinHashSignatureDistanceMetric(DataContainer context) {
        super(context);
    }

    private int[] signatureToIntArray( byte[] signature ){
        ByteBuffer byteBuffer = ByteBuffer.wrap(signature).order(ByteOrder.BIG_ENDIAN);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        int[] intArray = new int[intBuffer.remaining()];
        intBuffer.get(intArray);
        return intArray;
    }

    @Override
    public double getDistance(byte[] object_a, byte[] object_b) {
        int[] sig1 = signatureToIntArray(object_a);
        int[] sig2 = signatureToIntArray(object_b);

        //Adapted form the info.debatty.java.lsh.MinHash hash similarity implementation;
        if (sig1.length != sig2.length) {
            return -1; //Null value
        }

        double sim = 0;
        for (int i = 0; i < sig1.length; i++) {
            if (sig1[i] == sig2[i]) {
                sim += 1;
            }
        }

        return sim / sig1.length;
    }
}
