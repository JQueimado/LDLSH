package Factories.DataFactories;

import SystemLayer.Data.UniqueIndentifierImpl.Sha256UniqueIdentifier;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniqueIdentifierFactoryTest {

    @Test
    void getNewUniqueIdentifier_NONE() {
        UniqueIdentifierFactory uniqueIdentifierFactory = new UniqueIdentifierFactory();
        UniqueIdentifier uniqueIdentifier = uniqueIdentifierFactory.getNewUniqueIdentifier("NONE");
        assertNull(uniqueIdentifier);
    }

    @Test
    void getNewUniqueIdentifier_SHA256() {
        UniqueIdentifierFactory uniqueIdentifierFactory = new UniqueIdentifierFactory();
        UniqueIdentifier uniqueIdentifier = uniqueIdentifierFactory.getNewUniqueIdentifier("SHA256");
        assertNotNull(uniqueIdentifier);
        assertEquals(uniqueIdentifier.getClass(), Sha256UniqueIdentifier.class);
    }
}