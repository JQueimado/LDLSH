package SystemLayer.Containers;

import SystemLayer.Factories.*;
import SystemLayer.Processes.*;

public abstract class DataContainer {

    /*Objects*/
    private static ErasureProcessor erasureProcessor = null;
    private static ObjectNameProcessor objectNameProcessor = null;
    private static LSHProcessor lshProcessor = null;
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
    public static ObjectNameProcessor getObjectNameProcessorInstance(){
        if (objectNameProcessor == null)
            objectNameProcessor = ObjectNameProcessorFactory.getNewInstance();
        return objectNameProcessor;
    }

    //LSH Processor
    public static LSHProcessor getLshProcessorInstance(){
        if(lshProcessor == null)
            lshProcessor = LSHProcessorFactory.getNewInstance();
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
