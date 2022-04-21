package LSHTests;

import Factories.ComponentFactories.MultimapFactory;
import Factories.DataFactories.DataObjectFactory;
import Factories.DataFactories.LSHHashFactory;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.multi.MultiLabelUI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        MultimapFactory multimapFactory = appContext.getMultimapFactory();
        try {
            for ( int i = 0; i<b; i++) {
                multiMaps[i] = multimapFactory.getNewMultiMap(
                        configurator.getConfig("MULTIMAP"),
                        appContext
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
        DataObject dataObject = dataObjects[0];

        LSHHash hash = appContext.getLshHashFactory()
                .getNewLSHHash( configurator.getConfig("LSH_HASH"), appContext );
        hash.setObject(dataObject, multiMaps.length);

        UniqueIdentifier uid = appContext.getUniqueIdentifierFactory()
                .getNewUniqueIdentifier("SHA256");
        uid.setObject(dataObject);

        ErasureCodes erasureCodes = appContext.getErasureCodesFactory()
                .getNewErasureCodes("SIMPLE_PARTITION");
        erasureCodes.encodeDataObject(dataObject, multiMaps.length);

        //Insert
        for (MultiMap map : multiMaps) {
            map.insert(hash, uid, erasureCodes);
        }

        List<MultiMap.MultiMapValue> results = new ArrayList<>();

        //Query
        for (MultiMap multiMap : multiMaps) {
            MultiMap.MultiMapValue[] query_results = multiMap.query(hash);
            Collections.addAll(results, query_results);
        }

        //Assertions
        assertEquals( multiMaps.length, results.size() );
        for(MultiMap.MultiMapValue value : results){
            assertEquals( uid, value.uniqueIdentifier());
        }
    }
}
