package SystemLayer.Containers;

import Factories.ComponentFactories.DistanceMeasurerFactory;
import Factories.DataFactories.DataObjectFactory;
import Factories.DataFactories.ErasureCodesFactory;
import Factories.DataFactories.LSHHashFactory;
import Factories.DataFactories.UniqueIdentifierFactory;
import Factories.MessageFactory;
import SystemLayer.Components.DistanceMeasurerImpl.DistanceMeasurer;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.Configurator.Configurator;

import java.io.IOException;
import java.util.concurrent.ExecutorService;


public class DataContainer {

    private Configurator configurator = null;

    //Factories
    private DataObjectFactory dataObjectFactory = null;
    private LSHHashFactory lshHashFactory = null;
    private ErasureCodesFactory erasureCodesFactory = null;
    private UniqueIdentifierFactory uniqueIdentifierFactory = null;
    private MessageFactory messageFactory = null;

    //Components
    private MultiMap[] multiMaps;
    private ExecutorService executorService;
    private DistanceMeasurer distanceMeasurer = null;

    //Constructor
    public DataContainer( String f_name ){
        configurator = new Configurator(f_name);
    }

    //getters
    public Configurator getConfigurator(){
        return configurator;
    }

    //Creates Data Objects
    public DataObjectFactory getDataObjectFactory(){
        if(dataObjectFactory == null)
            dataObjectFactory = new DataObjectFactory();
        return dataObjectFactory;
    }

    //Creates LSHHash objects
    public LSHHashFactory getLshHashFactory(){
        if(lshHashFactory == null)
            lshHashFactory = new LSHHashFactory();
        return lshHashFactory;
    }

    public ErasureCodesFactory getErasureCodesFactory(){
        if (erasureCodesFactory == null)
            erasureCodesFactory = new ErasureCodesFactory();
        return erasureCodesFactory;
    }

    public UniqueIdentifierFactory getUniqueIdentifierFactory(){
        if(uniqueIdentifierFactory == null)
            uniqueIdentifierFactory = new UniqueIdentifierFactory(this);
        return uniqueIdentifierFactory;
    }

    public MessageFactory getMessageFactory(){
        if(messageFactory == null)
            messageFactory = new MessageFactory();
        return messageFactory;
    }

    //Components
    // -MultiMaps
    public void setMultiMaps( MultiMap[] multiMaps ){
        this.multiMaps = multiMaps;
    }

    public MultiMap[] getMultiMaps() throws Exception {
        return multiMaps;
    }

    //-Executor Service
    public void setExecutorService( ExecutorService executorService ){
        this.executorService = executorService;
    }
    public ExecutorService getExecutorService( ){
        return this.executorService;
    }

    //Distance Measurer
    public DistanceMeasurer getDistanceMeasurer(  ){
        if(distanceMeasurer == null){
            DistanceMeasurerFactory distanceMeasurerFactory = new DistanceMeasurerFactory();
            distanceMeasurer = distanceMeasurerFactory.getNewDistanceMeasurer(
                    configurator.getConfig("DISTANCE_METRIC")
            );
        }
        return distanceMeasurer;
    }

}
