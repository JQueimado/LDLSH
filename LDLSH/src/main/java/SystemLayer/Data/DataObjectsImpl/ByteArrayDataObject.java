package SystemLayer.Data.DataObjectsImpl;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ByteArrayDataObject implements DataObject<byte[]>{

    byte[] data;

    @Override
    public byte[] getValues() {
        return data;
    }

    @Override
    public void setValues(byte[] values) {
        data = values;
    }

    @Override
    public void setByteArray(byte[] array) {
        setValues(array);
    }

    @Override
    public byte[] toByteArray() {
        return getValues();
    }

    @Override
    public int objectByteSize(int dimensions) {
        return dimensions;
    }

    @Override
    public int compareTo(@NotNull DataObject<byte[]> o) {
        return Arrays.compare(data, o.toByteArray());
    }
}
