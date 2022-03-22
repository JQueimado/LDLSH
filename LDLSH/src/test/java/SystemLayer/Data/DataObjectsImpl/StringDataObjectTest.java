package SystemLayer.Data.DataObjectsImpl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringDataObjectTest {

    @Test
    void setGetValues() {
        String string = "This is a string for testing";
        DataObject object = new StringDataObject();
        object.setValues(string);
        String new_string = (String) object.getValues();
        assertNotNull(new_string);
        assertEquals( new_string, string );
    }

    @Test
    void toByteArray() {
        String string = "This is a string for testing";
        DataObject object = new StringDataObject();
        object.setValues(string);
        byte[] bytes = object.toByteArray();
        assertNotNull(bytes);
        assertEquals( bytes.length, 28 );
    }
}