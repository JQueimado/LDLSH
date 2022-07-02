package SystemLayer.Containers;

import Factories.CommunicationLayerFactory;
import Factories.ComponentFactories.DataProcessorFactory;
import Factories.ComponentFactories.DistanceMeasurerFactory;
import Factories.DataFactories.DataObjectFactory;
import Factories.DataFactories.ErasureCodesFactory;
import Factories.DataFactories.LSHHashFactory;
import Factories.DataFactories.UniqueIdentifierFactory;
import NetworkLayer.CommunicationLayer;
import SystemLayer.Components.DataProcessor.DataProcessor;
import SystemLayer.Components.DistanceMeasurerImpl.DistanceMeasurer;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.SystemExceptions.UnknownConfigException;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DataContainer {

    //Static
    public static final String nBands_config = "N_BANDS";
    public static final String dataSize_config = "VECTOR_SIZE";
    public static final String nThreads_config = "N_THREADS";
    public static final String nCallbackThreads_config = "N_CALLBACK_THREADS";

    //Factories
    private DataObjectFactory dataObjectFactory = null;
    private LSHHashFactory lshHashFactory = null;
    private ErasureCodesFactory erasureCodesFactory = null;
    private UniqueIdentifierFactory uniqueIdentifierFactory = null;

    //Components
    private final Configurator configurator;
    private MultiMap[] multiMaps;
    private ListeningExecutorService executorService;
    private DistanceMeasurer distanceMeasurer = null;
    private DataProcessor dataProcessor = null;
    private CommunicationLayer communicationLayer = null;
    private ExecutorService callbackExecutor = null;

    //Variables
    private int numberOfBands = -1;
    private int objectByteSize = -1;
    private int erasureCodesDataSize = -1;

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

    //Components
    // -MultiMaps
    public void setMultiMaps( MultiMap[] multiMaps ){
        this.multiMaps = multiMaps;
    }

    public MultiMap[] getMultiMaps() throws Exception {
        return multiMaps;
    }

    //-Executor Service
    public ListeningExecutorService getExecutorService( ){
        if (executorService == null){
            String nThreadString = "";
            try {
                nThreadString = configurator.getConfig( nThreads_config );
                int n_threads = Integer.parseInt( nThreadString );
                executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(n_threads));
            }catch (Exception e){
                UnknownConfigException.handler( new UnknownConfigException( nThreads_config, nThreadString ) );
            }
        }
        return this.executorService;
    }

    //CallBack Executor
    public ExecutorService getCallbackExecutor(){
        if (callbackExecutor == null){
            String nThreadString = "";
            try {
                nThreadString = configurator.getConfig( nCallbackThreads_config );
                int n_threads = Integer.parseInt( nThreadString );
                callbackExecutor = Executors.newFixedThreadPool(n_threads);
            }catch (Exception e){
                UnknownConfigException.handler( new UnknownConfigException( nThreads_config, nThreadString ) );
            }
        }
        return this.callbackExecutor;
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

    //Communication Layer
    public CommunicationLayer getCommunicationLayer(){
        if(communicationLayer == null){
            communicationLayer = (new CommunicationLayerFactory(this)).getNewCommunicationLayer();
        }
        return communicationLayer;
    }

    //Variables
    //Number of multi maps
    public int getNumberOfBands() {
        if( numberOfBands < 0 ) {
            numberOfBands = Integer.parseInt(configurator.getConfig(nBands_config));
        }
        return numberOfBands;
    }

    //Number of bytes each object contains
    public int getObjectByteSize(){
        if( objectByteSize == -1 ) {
            String value = "";
            try {
                value = configurator.getConfig(dataSize_config);
                DataObject temp = getDataObjectFactory().getNewDataObject();
                objectByteSize = temp.objectByteSize( Integer.parseInt(value) );
            } catch (Exception e) {
                UnknownConfigException.handler(new UnknownConfigException(dataSize_config, value));
            }
        }
        return objectByteSize;
    }

    //Erasure codes data size
    public void setErasureCodesDataSize(int size){
        if( erasureCodesDataSize == -1 )
            erasureCodesDataSize = size;
    }

    public int getErasureCodesDataSize(){
        return erasureCodesDataSize;
    }
}
