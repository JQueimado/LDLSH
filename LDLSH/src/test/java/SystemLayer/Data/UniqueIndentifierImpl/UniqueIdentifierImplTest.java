package SystemLayer.Data.UniqueIndentifierImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniqueIdentifierImplTest {

    private DataContainer appContext;
    private DataObject<String> object1;
    private DataObject<String> object2;

    @BeforeEach
    public void beforeEach(){
        object1 = new StringDataObject();
        object1.setValues("Some String");

        object2 = new StringDataObject();
        object2.setValues("Some String");

    }

    @Test
    void compareToEquals() {
        UniqueIdentifier uniqueIdentifier1 = new Sha256UniqueIdentifier(appContext);
        UniqueIdentifier uniqueIdentifier2 = new Sha256UniqueIdentifier(appContext);

        uniqueIdentifier1.setObject(object1.toByteArray());
        uniqueIdentifier2.setObject(object2.toByteArray());

        assertEquals(0, uniqueIdentifier1.compareTo(uniqueIdentifier2));
    }

    @Test
    void testEqualsTrue() {
        UniqueIdentifier uniqueIdentifier1 = new Sha256UniqueIdentifier(appContext);
        UniqueIdentifier uniqueIdentifier2 = new Sha256UniqueIdentifier(appContext);

        uniqueIdentifier1.setObject(object1.toByteArray());
        uniqueIdentifier2.setObject(object2.toByteArray());

        assertEquals(true, uniqueIdentifier1.equals(uniqueIdentifier2));
        assertEquals( uniqueIdentifier1, uniqueIdentifier2);
    }
}