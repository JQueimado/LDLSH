package SystemLayer.Containers;

import Factories.ComponentFactories.*;
import Factories.DataFactories.DataObjectFactory;
import Factories.DataFactories.ErasureCodesFactory;
import Factories.DataFactories.LSHHashFactory;
import Factories.DataFactories.UniqueIdentifierFactory;
import Factories.MessageFactory;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.TaskManager;
import SystemLayer.Containers.Configurator.Configurator;

import java.io.IOException;


public class DataContainer {

    private Configurator configurator = null;

    //Factories
    private DistanceMeasurerFactory distanceMeasurerFactory = null;
    private NodeFactory nodeFactory = null;
    private PackerFactory packerFactory = null;
    private PostProcessorFactory postProcessorFactory = null;
    private MultimapFactory multimapFactory = null;
    private DataObjectFactory dataObjectFactory = null;
    private LSHHashFactory lshHashFactory = null;
    private ErasureCodesFactory erasureCodesFactory = null;
    private UniqueIdentifierFactory uniqueIdentifierFactory = null;
    private MessageFactory messageFactory = null;

    //Components
    private MultiMap[] multiMaps;
    private TaskManager taskManager;

    //Constructor
    public DataContainer( String f_name ){
        configurator = new Configurator(f_name);
    }

    //getters
    public Configurator getConfigurator() throws IOException {
        return configurator;
    }

    public DistanceMeasurerFactory getDistanceMeasurerFactory() {
        if (distanceMeasurerFactory == null)
            distanceMeasurerFactory = new DistanceMeasurerFactory();
        return distanceMeasurerFactory;
    }

    public NodeFactory getNodeFactory(){
        if(nodeFactory == null)
            nodeFactory = new NodeFactory();
        return nodeFactory;
    }

    public PackerFactory getPackerFactory(){
        if(packerFactory == null)
            packerFactory = new PackerFactory();
        return packerFactory;
    }

    public PostProcessorFactory getPostProcessorFactory(){
        if (postProcessorFactory == null)
            postProcessorFactory = new PostProcessorFactory();
        return postProcessorFactory;
    }

    public MultimapFactory getMultimapFactory(){
        if (multimapFactory == null)
            multimapFactory = new MultimapFactory();
        return multimapFactory;
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
            uniqueIdentifierFactory = new UniqueIdentifierFactory();
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
    //-TaskManager
    public void setTaskManager( TaskManager taskManager ){
        this.taskManager = taskManager;
    }

    public TaskManager getTaskManager(){
        return this.taskManager;
    }
}
