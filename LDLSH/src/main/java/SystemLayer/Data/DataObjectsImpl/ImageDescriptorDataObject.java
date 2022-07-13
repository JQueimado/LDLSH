package SystemLayer.Data.DataObjectsImpl;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class ImageDescriptorDataObject implements DataObject<float[]> {

    byte[] data;

    @Override
    public float[] getValues() {
        return new float[0];
    }

    @Override
    public void setValues(float[] values) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            for( float f : values )
                oos.writeFloat(f);
            data = bos.toByteArray();
        }catch (IOException ioe){
            ioe.printStackTrace();
            data = null;
        }
    }

    @Override
    public void setByteArray(byte[] array) {
        data=array;
    }

    @Override
    public byte[] toByteArray() {
        return data;
    }

    @Override
    public int objectByteSize(int dimensions) {
        return dimensions*Float.BYTES;
    }

    @Override
    public int compareTo(@NotNull DataObject<float[]> o) {
        return Arrays.compare( data, o.toByteArray() );
    }
}
