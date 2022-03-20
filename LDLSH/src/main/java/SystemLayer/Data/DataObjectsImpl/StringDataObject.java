package SystemLayer.Data.DataObjectsImpl;

import java.nio.charset.StandardCharsets;

public class StringDataObject implements DataObject{

    private String data;

    public StringDataObject(String value){
        data = value;
    }
    public StringDataObject(){
        this("");
    }

    @Override
    public Object getValues() {
        return data;
    }

    @Override
    public void setValues(Object values){
        data = (String) values;
    }

    @Override
    public byte[] toByteArray() {
        return data.getBytes(StandardCharsets.UTF_8);
    }
}
