package SystemLayer.Components.DistanceMetricImpl;

import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.DataObjectsImpl.StringDataObject;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinHashSignatureDistanceMetricTest {

    DataContainer simulatedState;
    DataObject<String>[] dataObjects;

    @BeforeEach
    public void before() throws Exception{
        //State
        simulatedState = new DataContainer("");
        Configurator configurator = simulatedState.getConfigurator();
        configurator.setConfig("N_BANDS", "3");
        configurator.setConfig("THRESHOLD", "0.95");
        configurator.setConfig("VECTOR_DIMENSIONS", "5");
        configurator.setConfig("LSH_SEED", "11111");
        configurator.setConfig("LSH_HASH", "JAVA_MINHASH_NGRAMS");
        configurator.setConfig("NGRAMS_LEVEL", "3");

        configurator.setConfig("DISTANCE_METRIC", "MINHASH_SIGNATURE");

        //Vectors
        String data_object_type = "STRING";
        DataObjectFactory dataObjectFactory = simulatedState.getDataObjectFactory();
        dataObjects = new StringDataObject[4];

        dataObjects[0] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[0].setValues("==1ADDD?2,2+2,AEDEAD>:ACFE+A?AACFEEC@9E3C?*CDD9B9?D=<**0??4D?B@;))=.==@C@CA)=.)=;)7?@AA@@2.>@;;>A;>>");

        //Jaccard Distance to 0: 0.2
        dataObjects[1] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[1].setValues("+=1ADDD;:222A:EE93CEEF3++3<9CCC<EC>EDFCC?C*BBDD>@DEIDBDD<?B/8/8=B@##################################");

        //Jaccard Distance to 0: 0.0
        dataObjects[2] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[2].setValues("1=;+A=D=DD?:++222AF+AF9AEE)?9:*11):D):?)889?A#######################################################");

        //Jaccard Distance to 0: 0.8
        dataObjects[3] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        dataObjects[3].setValues("1::ADD@D?)+222AEE?C22:):C?D?):*0:*???BD899B;AAAADCAA=C;A)=@37?)7?###################################");

        //dataObjects[4] = (StringDataObject) dataObjectFactory.getNewDataObject( data_object_type );
        //dataObjects[4].setValues("54321");
    }

    private double calc_error( int byte_size ){
        return 1.0/Math.sqrt( byte_size/4.0 ); //Based on the debatty.MinHash.error calculation
    }

    private void getDistance_template( DataObject<?> o1, DataObject<?> o2, double expected ) {
        LSHHash hash1 = simulatedState.getLshHashFactory().getNewLSHHash();
        hash1.setObject(o1.toByteArray(), 1);

        LSHHash hash2 = simulatedState.getLshHashFactory().getNewLSHHash();
        hash2.setObject(o2.toByteArray(), 1);

        double distance = simulatedState.getDistanceMeasurer().getDistance(
                hash1.getSignature(),
                hash2.getSignature() );

        double error = calc_error(hash1.getSignature().length);

        assertEquals(expected, distance, error);
    }

    @Test
    void getDistance_1() {
        LSHHash hash1 = simulatedState.getLshHashFactory().getNewLSHHash();
        hash1.setObject(dataObjects[0].toByteArray(), 1);

        double distance = simulatedState.getDistanceMeasurer().getDistance(
                hash1.getSignature(),
                hash1.getSignature() );

        assertEquals(0, distance);
    }

    @Test
    void getDistance_2(){
        getDistance_template(
                dataObjects[0],
                dataObjects[1],
                0.9745222929936306
        );
    }

    @Test
    void getDistance_3(){
        getDistance_template(
                dataObjects[0],
                dataObjects[2],
                0.9928571428571429
        );
    }

    @Test
    void getDistance_4(){
        getDistance_template(
                dataObjects[0],
                dataObjects[3],
                0.9807692307692307
        );
    }

    @Test
    void getDistance_5(){
        getDistance_template(
                dataObjects[1],
                dataObjects[2],
                0.9724770642201834
        );
    }

    @Test
    void getDistance_6(){
        getDistance_template(
                dataObjects[1],
                dataObjects[3],
                0.9682539682539683
        );
    }

    @Test
    void getDistance_7(){
        getDistance_template(
                dataObjects[2],
                dataObjects[3],
                0.9523809523809523
        );
    }
}