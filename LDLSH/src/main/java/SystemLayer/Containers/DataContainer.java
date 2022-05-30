package SystemLayer.Containers;

import Factories.ComponentFactories.DataProcessorFactory;
import Factories.ComponentFactories.DistanceMeasurerFactory;
import Factories.DataFactories.DataObjectFactory;
import Factories.DataFactories.ErasureCodesFactory;
import Factories.DataFactories.LSHHashFactory;
import Factories.DataFactories.UniqueIdentifierFactory;
import Factories.Factory;
import Factories.MessageFactory;
import SystemLayer.Components.DataProcessor.DataProcessor;
import SystemLayer.Components.DistanceMeasurerImpl.DistanceMeasurer;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.SystemExceptions.UnknownConfigException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;


public class DataContainer {

    //Static
    private static final String nBands_config = "N_BANDS";

    //Factories
    private DataObjectFactory dataObjectFactory = null;
    private LSHHashFactory lshHashFactory = null;
    private ErasureCodesFactory erasureCodesFactory = null;
    private UniqueIdentifierFactory uniqueIdentifierFactory = null;
    private MessageFactory messageFactory = null;

    //Components
    private Configurator configurator = null;
    private MultiMap[] multiMaps;
    private ExecutorService executorService;
    private DistanceMeasurer distanceMeasurer = null;
    private DataProcessor dataProcessor = null;

    //Variables
    private int numberOfBands = -1;

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
            dataObjectFactory = new DataObjectFactory(this);
        return dataObjectFactory;
    }

    //Creates LSHHash objects
    public LSHHashFactory getLshHashFactory(){
        if(lshHashFactory == null)
            lshHashFactory = new LSHHashFactory(this);
        return lshHashFactory;
    }

    public ErasureCodesFactory getErasureCodesFactory(){
        if (erasureCodesFactory == null)
            erasureCodesFactory = new ErasureCodesFactory(this);
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

    //DataProcessor
    public DataProcessor getDataProcessor(){
        if( dataProcessor == null ){
            try {
                DataProcessorFactory dataProcessorFactory = new DataProcessorFactory(this);
                dataProcessor = dataProcessorFactory.getNewDataProcessor();
            }catch (UnknownConfigException uce){
                UnknownConfigException.handler(uce);
            }
        }
        return dataProcessor;
    }

    //Variables
    //Number of multi maps
    public int getNumberOfBands() {
        if( numberOfBands < 0 ) {
            numberOfBands = Integer.parseInt(configurator.getConfig(nBands_config));
        }
        return numberOfBands;
    }
}
