package LSHTests;

import Factories.ComponentFactories.MultimapFactory;
import Factories.DataFactories.DataObjectFactory;
import Factories.DataFactories.LSHHashFactory;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.multi.MultiLabelUI;
import java.io.IOException;

public class LocalLSHTests {

    DataContainer appContext;
    Configurator configurator;
    LSHHashFactory lshHashFactory;
    DataObject[] dataObjects;
    MultiMap[] multiMaps;

    @BeforeAll
    public void before() throws IOException {
        appContext = new DataContainer("");
        configurator = appContext.getConfigurator();
        lshHashFactory = appContext.getLshHashFactory();

        configurator.setConfig("ERROR", "0.1");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "11111");
        configurator.setConfig("LSH_HASH", "JAVA_MINHASH");
        configurator.setConfig("N_MULTIMAPS", "2");
        configurator.setConfig("MULTIMAPS", "GUAVA_MEMORY_MULTIMAP");

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
        int N = Integer.parseInt( configurator.getConfig("N_MULTIMAPS") );
        multiMaps = new MultiMap[N];
        MultimapFactory multimapFactory = appContext.getMultimapFactory();
        try {
            for ( int i = 0; i<N; i++)
                multiMaps[i] = multimapFactory.getNewMultiMap(configurator.getConfig("MULTIMAP"), appContext);
        }catch (Exception e ){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void InsertMapTest(){
        //LSHHash hash = lshHashFactory.getNewLSHHash( configurator.getConfig("LSH_HASH"), appContext );
        //hash.setObject(dataObjects[0], n_multimaps );
    }
}
