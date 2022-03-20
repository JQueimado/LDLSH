package SystemLayer.Containers;

import Factories.ComponentFactories.*;
import Factories.DataFactories.DataObjectFactory;
import Factories.DataFactories.LSHHashFactory;
import Factories.MessageFactory;
import SystemLayer.Configurator.Configurator;
import SystemLayer.Processes.*;
import SystemLayer.Processes.ErasureProcessorImpl.ErasureProcessor;
import SystemLayer.Processes.LSHHashFactory.LSHHashProcessor;
import SystemLayer.Processes.Nodes.Node;
import SystemLayer.Processes.PostProcessorImpl.PostProcessor;

import java.io.FileReader;
import java.io.IOException;
import java.util.PrimitiveIterator;
import java.util.Properties;

public class DataContainer {

    private Configurator configurator = null;

    //ComponentFactories
    private DistanceMeasurerFactory distanceMeasurerFactory = null;
    private ErasureProcessorFactory erasureProcessorFactory = null;
    private LSHHashProcessorFactory lshHashProcessorFactory = null;
    private NodeFactory nodeFactory = null;
    private PackerFactory packerFactory = null;
    private PostProcessorFactory postProcessorFactory = null;
    private UniqueIdentifierProcessorFactory uniqueIdentifierProcessorFactory = null;

    //Data Factories
    private DataObjectFactory dataObjectFactory = null;
    private LSHHashFactory lshHashFactory = null;

    //Message
    private MessageFactory messageFactory = null;

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

    public ErasureProcessorFactory getErasureProcessorFactory(){
        if (erasureProcessorFactory == null)
            erasureProcessorFactory = new ErasureProcessorFactory();
        return erasureProcessorFactory;
    }

    public LSHHashProcessorFactory getLshHashProcessor(){
        if(lshHashProcessorFactory == null)
            lshHashProcessorFactory = new LSHHashProcessorFactory();
        return lshHashProcessorFactory;
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

    public UniqueIdentifierProcessorFactory getUniqueIdentifierProcessorFactory(){
        if(uniqueIdentifierProcessorFactory == null)
            uniqueIdentifierProcessorFactory = new UniqueIdentifierProcessorFactory();
        return uniqueIdentifierProcessorFactory;
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

    public MessageFactory getMessageFactory(){
        if(messageFactory == null)
            messageFactory = new MessageFactory();
        return messageFactory;
    }
}
