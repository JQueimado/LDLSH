package Factories.DataFactories;

import SystemLayer.Data.ErasureCodesImpl.BlackblazeReedSolomonErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErasureCodesFactoryTest {

    @Test
    void getNewProcessor_None() throws Exception {
        ErasureCodesFactory erasureCodesFactory = new ErasureCodesFactory();
        ErasureCodes erasureCodes = erasureCodesFactory.getNewErasureCodes("NONE", null);
        assertNull( erasureCodes );
    }

    void getNewProcessor_REED_SOLOMON() throws Exception {
        ErasureCodesFactory erasureCodesFactory = new ErasureCodesFactory();
        ErasureCodes erasureCodes = erasureCodesFactory.getNewErasureCodes("REED_SOLOMON", null);
        assertNotNull(erasureCodes);
        assertEquals( erasureCodes.getClass(), BlackblazeReedSolomonErasureCodes.class);
    }
}