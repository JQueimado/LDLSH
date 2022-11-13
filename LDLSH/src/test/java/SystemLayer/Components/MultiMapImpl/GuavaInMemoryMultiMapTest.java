package SystemLayer.Components.MultiMapImpl;

import Factories.ComponentFactories.MultimapFactory;
import SystemLayer.Data.DataUnits.ModelMultimapValue;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.LSHHashImpl.JavaMinHash;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.DataUnits.LSHHashBlock;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GuavaInMemoryMultiMapTest {

    DataContainer simulatedState;
    MultimapFactory multimapFactory;

    List<DataObject<String>> objects;
    List<LSHHash> hashes;
    List<UniqueIdentifier> uniqueIdentifiers;
    List<ErasureCodes> erasureCodes;

    @BeforeEach
    void before() throws Exception {
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("UNIQUE_IDENTIFIER", "SHA256");
        configurator.setConfig("ERASURE_CODES",     "SIMPLE_PARTITION");
        configurator.setConfig("THRESHOLD",         "0.50");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_HASH",          "JAVA_MINHASH");
        configurator.setConfig("LSH_SEED",          "11111");
        configurator.setConfig("MULTIMAP",          "GUAVA_MEMORY_MULTIMAP");
        configurator.setConfig("N_BANDS",           "1");

        multimapFactory = new MultimapFactory(simulatedState);

        //Objects
        objects = new ArrayList<>();
        hashes = new ArrayList<>();
        uniqueIdentifiers = new ArrayList<>();
        erasureCodes = new ArrayList<>();

        Random random = new Random();
        DataObject<String> object;
        LSHHash hash;
        ErasureCodes erasureCode;
        UniqueIdentifier uniqueIdentifier;

        int subjects = 100;
        for (int i = 0; i<subjects; i++){
            //Object
            object = new StringDataObject();
            StringBuilder stringBuilder = new StringBuilder();
            for(int j = 0; j<100; j++){
                stringBuilder.append(random.nextInt(0, 9));
            }
            object.setValues( stringBuilder.toString() );
            objects.add(object);

            //Hash
            hash = simulatedState.getLshHashFactory().getNewLSHHash();
            hash.setObject( object.toByteArray(), 1 );
            hashes.add(hash);

            //Erasure Codes
            erasureCode = simulatedState.getErasureCodesFactory().getNewErasureCodes();
            erasureCode.encodeDataObject( object.toByteArray(), 1 );
            erasureCodes.add( erasureCode );

            //UID
            uniqueIdentifier = simulatedState.getUniqueIdentifierFactory().getNewUniqueIdentifier();
            uniqueIdentifier.setObject( object.toByteArray() );
            uniqueIdentifiers.add( uniqueIdentifier );
        }

    }

    @Test
    void testGetBlock() throws Exception {
        MultiMap multiMap = multimapFactory.getNewMultiMap();
        multiMap.setHashBlockPosition(0);
        multiMap.setTotalBlocks(1);
        int position = 0;

        ModelMultimapValue modelMultimapValue = new ModelMultimapValue(
                hashes.get(position),
                uniqueIdentifiers.get(position),
                erasureCodes.get(position).getBlockAt(0)
                );

        multiMap.insert(
                hashes.get(position),
                modelMultimapValue
        );

        LSHHash new_hash = new JavaMinHash(objects.get(position), 1, simulatedState);
        LSHHashBlock block = multiMap.getBlock( new_hash );
        assertNotNull(block);
        assertArrayEquals( hashes.get(position).getBlockAt(0).lshBlock(), block.lshBlock() );
    }

    @Test
    void insertQueryTest() throws Exception {
        MultiMap multiMap = multimapFactory.getNewMultiMap();
        multiMap.setHashBlockPosition(0);
        multiMap.setTotalBlocks(1);
        int position = 0;

        ModelMultimapValue modelMultimapValue = new ModelMultimapValue(
                hashes.get(position),
                uniqueIdentifiers.get(position),
                erasureCodes.get(position).getBlockAt(0)
        );

        multiMap.insert(
                hashes.get(position),
                modelMultimapValue
        );

        MultiMapValue[] values = multiMap.query(hashes.get(position).getBlockAt(0));
        assertEquals(1, values.length);

        ModelMultimapValue value = (ModelMultimapValue) values[0];
        assertEquals(uniqueIdentifiers.get(position), value.uniqueIdentifier());
        assertEquals(hashes.get(position), value.lshHash());
        //TODO: Erasure codes check
    }

    @Test
    void complete() throws Exception {
        MultiMap multiMap = multimapFactory.getNewMultiMap();
        multiMap.setHashBlockPosition(0);
        multiMap.setTotalBlocks(1);

        //Insert
        for( int i = 0; i<100; i++ ){
            ModelMultimapValue modelMultimapValue = new ModelMultimapValue(
                    hashes.get(i),
                    uniqueIdentifiers.get(i),
                    erasureCodes.get(i).getBlockAt(0)
            );

            multiMap.insert(hashes.get(i), modelMultimapValue);
        }

        Random random = new Random();
        int subject = random.nextInt(0,100);

        LSHHash hash = hashes.get(subject);
        UniqueIdentifier uid = uniqueIdentifiers.get(subject);
        ErasureCodes erasureCode = erasureCodes.get(subject);

        ErasureCodesImpl.ErasureBlock erasureBlock =  multiMap.complete(hash, uid);
        ErasureCodes erasureCodesResult = simulatedState.getErasureCodesFactory().getNewErasureCodes();
        erasureCodesResult.addBlockAt(erasureBlock);

        assertEquals( erasureCode, erasureCodesResult );
    }

    @Test
    void query() {
    }
}