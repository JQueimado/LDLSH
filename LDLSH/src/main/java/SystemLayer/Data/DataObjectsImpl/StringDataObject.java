package SystemLayer.Data.DataObjectsImpl;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class StringDataObject implements DataObject<String>{

    private byte[] data;

    public StringDataObject(String value){
        setValues(value);
    }

    public StringDataObject(){
        this("");
    }

    @Override
    public String getValues() {
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public void setValues(String values){
        data = values.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void setByteArray(byte[] array) {
        data = array;
    }

    @Override
    public byte[] toByteArray() {
        return data;
    }

    @Override
    public int objectByteSize(int dimensions) {
        String s = ("0".repeat(Math.max(0, dimensions)));
        return s.getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public int compareTo( @NotNull DataObject<String> o) {
        return Arrays.compare( data, o.toByteArray() );
    }

    @Override
    public boolean equals(Object obj) {
        if( obj.getClass() != StringDataObject.class )
            return false;
        return compareTo( (StringDataObject) obj ) == 0;
    }
}
