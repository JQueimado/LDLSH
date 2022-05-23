package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Components.MultiMapImpl.MultiMap.MultiMapValue;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.SimplePartitionErasureCodes;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHash.LSHHashBlock;
import SystemLayer.Data.UniqueIndentifierImpl.Sha256UniqueIdentifier;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuavaInMemoryMultiMapTest {

    DataContainer simulatedState;
    List<DataObject> objects;
    List<LSHHash> hashes;
    List<UniqueIdentifier> uniqueIdentifiers;
    List<ErasureCodes> erasureCodes;

    @BeforeEach
    void before() throws Exception {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("ERROR", "0.1");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "11111");
        configurator.setConfig("N_BANDS", "1");

        //Objects
        objects = new ArrayList<>();
        hashes = new ArrayList<>();
        uniqueIdentifiers = new ArrayList<>();
        erasureCodes = new ArrayList<>();

        DataObject object;
        LSHHash hash;
        ErasureCodes erasureCode;
        UniqueIdentifier uniqueIdentifier;

        //Object 0
        object = new StringDataObject("12345");
        hash = new JavaMinHash(object, 1, simulatedState);
        erasureCode = new SimplePartitionErasureCodes(simulatedState);
        uniqueIdentifier = new Sha256UniqueIdentifier();
        objects.add(object);
        hashes.add(hash);
        erasureCodes.add(erasureCode);
        uniqueIdentifiers.add(uniqueIdentifier);

    }

    @Test
    void testGetBlock(){
        MultiMap multiMap = new GuavaInMemoryMultiMap(0, 1);
        int position = 0;
        multiMap.insert(
                hashes.get(position),
                uniqueIdentifiers.get(position),
                erasureCodes.get(position)
        );

        LSHHash new_hash = new JavaMinHash(objects.get(position), 1, simulatedState);
        LSHHashBlock block = multiMap.getBlock( new_hash );
        assertNotNull(block);
        assertArrayEquals( hashes.get(position).getBlockAt(0).lshBlock(), block.lshBlock() );
    }

    @Test
    void insertQueryTest() {
        MultiMap multiMap = new GuavaInMemoryMultiMap(0,1);
        int position = 0;
        multiMap.insert(
                hashes.get(position),
                uniqueIdentifiers.get(position),
                erasureCodes.get(position)
        );

        MultiMapValue[] values = multiMap.query(hashes.get(position));
        assertEquals(1, values.length);

        MultiMapValue value = values[0];
        assertEquals(uniqueIdentifiers.get(position), value.uniqueIdentifier());
        assertEquals(hashes.get(position), value.lshHash());
        //TODO: Erasure codes check
    }

    @Test
    void complete() {
    }

    @Test
    void query() {
    }
}