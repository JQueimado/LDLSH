package SystemLayer.Data.DataObjectsImpl;

import java.nio.charset.StandardCharsets;

public class StringDataObject implements DataObject<String>{

    private String data;

    public StringDataObject(String value){
        data = value;
    }
    public StringDataObject(){
        this("");
    }

    @Override
    public String getValues() {
        return data;
    }

    @Override
    public void setValues(String values){
        data = (String) values;
    }

    @Override
    public void setByteArray(byte[] array) {
        data = new String(array, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] toByteArray() {
        return data.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public int compareTo( DataObject<String> o) {
        return this.getValues().compareTo(o.getValues());
    }
}
