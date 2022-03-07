package SystemLayer.Containers;

import Factories.ComponentFactories.*;
import SystemLayer.ProcessInterfacesses.*;

public abstract class DataContainer {

    /*Objects*/
    //SystemComponents
    private static ErasureProcessor erasureProcessor = null;
    private static UniqueIdentifierProcessor objectNameProcessor = null;
    private static LSHHashProcessor lshProcessor = null;
    private static Packer packer = null;
    private static PostProcessor postProcessor = null;
    private static DistanceMeasurer distanceMeasurer = null;

    /*Methods*/
    //Erasure processor
    public static ErasureProcessor getErasureProcessorInstance(){
        if( erasureProcessor == null )
            erasureProcessor = ErasureProcessorFactory.getNewInstance();
        return erasureProcessor;
    }

    //Object Name Processor
    public static UniqueIdentifierProcessor getObjectNameProcessorInstance(){
        if (objectNameProcessor == null)
            objectNameProcessor = UniqueIdentifierProcessorFactory.getNewInstance();
        return objectNameProcessor;
    }

    //LSH Processor
    public static LSHHashProcessor getLshProcessorInstance(){
        if(lshProcessor == null)
            lshProcessor = LSHHashProcessorFactory.getNewInstance();
        return lshProcessor;
    }

    //Packer
    public static Packer getPackerInstance(){
        if( packer == null )
            packer = PackerFactory.getNewInstance();
        return packer;
    }

    //Post Processor
    public static PostProcessor getPostProcessorInstance(){
        if (postProcessor == null)
            postProcessor = PostProcessorFactory.getNewInstance();
        return postProcessor;
    }

    public static DistanceMeasurer getDistanceMeasurerInstance(){
        if (distanceMeasurer == null)
            distanceMeasurer = DistanceMeasurerFactory.getNewDistanceMeasurer();
        return distanceMeasurer;
    }

}
