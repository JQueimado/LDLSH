package LSHTests;

import Factories.ComponentFactories.MultimapFactory;
import Factories.DataFactories.DataObjectFactory;
import Factories.DataFactories.LSHHashFactory;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LocalLSHTests {

    DataContainer appContext;
    Configurator configurator;
    LSHHashFactory lshHashFactory;
    DataObject[] dataObjects;
    MultiMap[] multiMaps;

    @BeforeEach
    public void before() throws IOException {
        appContext = new DataContainer("");
        configurator = appContext.getConfigurator();

        configurator.setConfig("ERROR", "0.1");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "11111");
        configurator.setConfig("LSH_HASH", "JAVA_MINHASH");
        configurator.setConfig("MULTIMAP", "GUAVA_MEMORY_MULTIMAP");
        configurator.setConfig("N_BANDS", "2");

        //Data objects
        dataObjects = new DataObject[5];
        DataObjectFactory dataObjectFactory = appContext.getDataObjectFactory();
        String data_object_type = "STRING";

        dataObjects[0] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[0].setValues("12345");

        //Jaccard Distance to 0: 0.2
        dataObjects[1] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[1].setValues("12355");

        //Jaccard Distance to 0: 0.0
        dataObjects[2] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[2].setValues("12345");

        //Jaccard Distance to 0: 0.8
        dataObjects[3] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[3].setValues("55555");

        dataObjects[4] = dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[4].setValues("54321");

        //Multimaps
        int b = Integer.parseInt( configurator.getConfig("N_BANDS") );

        multiMaps = new MultiMap[b];
        MultimapFactory multimapFactory = new MultimapFactory();
        try {
            for ( int i = 0; i<b; i++) {
                multiMaps[i] = multimapFactory.getNewMultiMap(
                        configurator.getConfig("MULTIMAP")
                );
                multiMaps[i].setTotalBlocks(b);
                multiMaps[i].setHashBlockPosition(i);
            }
        }catch (Exception e ){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void InsertMapTest(){
        // Insert object 0
        DataObject object = dataObjects[0];
        insert( object, multiMaps );

        //Run Query
        List<MultiMap.MultiMapValue> results = query( object, multiMaps );

        //Preprocess assertion
        UniqueIdentifier uid = appContext.getUniqueIdentifierFactory()
                .getNewUniqueIdentifier("SHA256");
        uid.setObject(object);

        //Assertions (All multi-maps should return the same object)
        Assertions.assertEquals(multiMaps.length, results.size());
        for(MultiMap.MultiMapValue value : results){
            Assertions.assertEquals( 0,uid.compareTo( value.uniqueIdentifier()));
        }
    }

    @Test
    public void SimilarityTest(){
        //Insert object 0
        insert(dataObjects[0], multiMaps);
        //Insert object 1
        insert(dataObjects[1], multiMaps );
    }

    private void insert(DataObject object, MultiMap[] multiMaps ){
        LSHHash hash = appContext.getLshHashFactory()
                .getNewLSHHash( configurator.getConfig("LSH_HASH"), appContext );
        hash.setObject(object, multiMaps.length);

        UniqueIdentifier uid = appContext.getUniqueIdentifierFactory()
                .getNewUniqueIdentifier("SHA256");
        uid.setObject(object);

        ErasureCodes erasureCodes = appContext.getErasureCodesFactory()
                .getNewErasureCodes("SIMPLE_PARTITION");
        erasureCodes.encodeDataObject(object, multiMaps.length);

        //Insert
        for (MultiMap map : multiMaps) {
            map.insert(hash, uid, erasureCodes);
        }
    }

    private List<MultiMap.MultiMapValue> query(DataObject queryObject, MultiMap[] multiMaps ){
        LSHHash hash = appContext.getLshHashFactory()
                .getNewLSHHash( configurator.getConfig("LSH_HASH"), appContext );
        hash.setObject(queryObject, multiMaps.length);

        UniqueIdentifier uid = appContext.getUniqueIdentifierFactory()
                .getNewUniqueIdentifier("SHA256");
        uid.setObject(queryObject);

        List<MultiMap.MultiMapValue> results = new ArrayList<>();

        //Query
        for (MultiMap multiMap : multiMaps) {
            MultiMap.MultiMapValue[] query_results = multiMap.query(hash);
            Collections.addAll(results, query_results);
        }

        return results;
    }
}
