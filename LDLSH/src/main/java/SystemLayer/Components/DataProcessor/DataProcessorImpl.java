package SystemLayer.Components.DataProcessor;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

public abstract class DataProcessorImpl implements DataProcessor {

    protected DataContainer appContext;

    public DataProcessorImpl( DataContainer appContext ){
        this.appContext = appContext;
    }

    @Override
    public ProcessedData preProcessData(DataObject object){ return null; }

    @Override
    public DataObject postProcess(){ return null; }
}
