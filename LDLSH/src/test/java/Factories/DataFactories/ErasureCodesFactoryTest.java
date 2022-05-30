package Factories.DataFactories;

import SystemLayer.Data.ErasureCodesImpl.BlackblazeReedSolomonErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErasureCodesFactoryTest {

    @Test
    void getNewProcessor_None(){
        ErasureCodesFactory erasureCodesFactory = new ErasureCodesFactory(null);
        ErasureCodes erasureCodes = erasureCodesFactory.getNewErasureCodes("NONE");
        assertNull( erasureCodes );
    }

    @Test
    void getNewProcessor_REED_SOLOMON(){
        ErasureCodesFactory erasureCodesFactory = new ErasureCodesFactory(null);
        ErasureCodes erasureCodes = erasureCodesFactory.getNewErasureCodes("REED_SOLOMON");
        assertNotNull(erasureCodes);
        assertEquals( erasureCodes.getClass(), BlackblazeReedSolomonErasureCodes.class);
    }
}