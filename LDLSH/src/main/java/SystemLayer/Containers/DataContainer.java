package SystemLayer.Containers;

import Factories.ComponentFactories.CommunicationLayerFactory;
import Factories.ComponentFactories.DataProcessorFactory;
import Factories.ComponentFactories.DistanceMetricFactory;
import Factories.ComponentFactories.MultimapTaskFactory;
import Factories.ComponentFactories.WorkerTaskFactory;
import Factories.DataFactories.DataObjectFactory;
import Factories.DataFactories.ErasureCodesFactory;
import Factories.DataFactories.LSHHashFactory;
import Factories.DataFactories.UniqueIdentifierFactory;
import SystemLayer.Components.NetworkLayer.CommunicationLayer;
import SystemLayer.Components.DataProcessor.DataProcessor;
import SystemLayer.Components.DistanceMetricImpl.DistanceMetric;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.SystemExceptions.UnknownConfigException;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.*;


public class DataContainer {

    //Static
    public static final String nBands_config = "N_BANDS";
    public static final String dataSize_config = "VECTOR_SIZE";
    public static final String nThreads_config = "PROCESS_THREADS";
    public static final String nCallbackThreads_config = "CALLBACK_THREADS";
    private static final String debug_config = "DEBUG";

    //Factories
    private DataObjectFactory dataObjectFactory = null;
    private LSHHashFactory lshHashFactory = null;
    private ErasureCodesFactory erasureCodesFactory = null;
    private UniqueIdentifierFactory uniqueIdentifierFactory = null;
    private MultimapTaskFactory multimapTaskFactory = null;
    private WorkerTaskFactory workerTaskFactory = null;

    //Components
    private final Configurator configurator;
    private List<MultiMap> multiMaps;
    private ListeningExecutorService executorService;
    private DistanceMetric distanceMeasurer = null;
    private DataProcessor dataProcessor = null;
    private CommunicationLayer communicationLayer = null;
    private ExecutorService callbackExecutor = null;

    private Object[] additionalStructures = null;

    //Variables
    //private boolean debug;
    private int numberOfBands = -1;

    //Constructor
    public DataContainer( String f_name ){
        configurator = new Configurator(f_name);
    //    try {
    //        debug = Boolean.parseBoolean(configurator.getConfig(debug_config));
    //    }catch (Exception e){
    //        debug = false;
    //    }
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

    public MultimapTaskFactory getMultimapTaskFactory(){
        if(multimapTaskFactory == null)
            multimapTaskFactory = new MultimapTaskFactory(this);
        return multimapTaskFactory;
    }

    public WorkerTaskFactory getWorkerTaskFactory(){
        if(workerTaskFactory == null)
            workerTaskFactory = new WorkerTaskFactory(this);
        return workerTaskFactory;
    }

    //Components
    // -MultiMaps
    public void setMultiMaps(List<MultiMap> multiMaps ){
        this.multiMaps = multiMaps;
    }

    public List<MultiMap> getMultiMaps() throws Exception {
        return multiMaps;
    }

    //-Executor Service
    public ListeningExecutorService getExecutorService( ){
        if (executorService == null){
            String nThreadString = "";
            try {
                nThreadString = configurator.getConfig( nThreads_config );
                int n_threads = Integer.parseInt( nThreadString );
                ExecutorService baseExecutorService = new ThreadPoolExecutor(
                        n_threads,
                        n_threads,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new LimitedBlockingQueue<>(n_threads * 2)
                        );
                executorService = MoreExecutors.listeningDecorator( baseExecutorService );
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
                UnknownConfigException.handler( new UnknownConfigException( nCallbackThreads_config, nThreadString ) );
            }
        }
        return this.callbackExecutor;
    }

    //Distance Measurer
    public DistanceMetric getDistanceMeasurer(  ){
        if(distanceMeasurer == null){
            DistanceMetricFactory distanceMetricFactory = new DistanceMetricFactory(this);
            distanceMeasurer = distanceMetricFactory.getNewDistanceMeasurer(
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

    public Object[] getAdditionalStructures(){
        return additionalStructures;
    }

    public void setAdditionalStructures(Object[] structures){
        additionalStructures = structures;
    }

    //Variables
    //Debug variable
    //public boolean getDebug(){
    //    return debug;
    //}

    //Number of multi maps
    public int getNumberOfBands() {
        if( numberOfBands < 0 ) {
            numberOfBands = Integer.parseInt(configurator.getConfig(nBands_config));
        }
        return numberOfBands;
    }

    private class LimitedBlockingQueue<E> extends LinkedBlockingQueue<E>{
        public LimitedBlockingQueue (int size){
            super(size);
        }

        @Override
        public boolean offer(@NotNull E e) {
            try {
                super.put(e);
                return true;
            }catch (InterruptedException ie){
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }
}
