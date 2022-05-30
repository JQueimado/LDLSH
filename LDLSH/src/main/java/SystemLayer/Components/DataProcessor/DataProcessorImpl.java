package SystemLayer.Components.DataProcessor;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public abstract class DataProcessorImpl implements DataProcessor {

    protected DataContainer appContext;

    public DataProcessorImpl( DataContainer appContext ){
        this.appContext = appContext;
    }

    @Override
    public abstract ProcessedData preProcessData(DataObject object) throws Exception;

    @Override
    public abstract DataObject postProcess( ErasureCodes erasureCodes, UniqueIdentifier uniqueIdentifier ) throws Exception;

    //Auxiliary methods
    protected boolean validate( byte [] data, UniqueIdentifier validation_uid ){
        UniqueIdentifier data_uid = appContext.getUniqueIdentifierFactory().getNewUniqueIdentifier();
        data_uid.setObject( data );
        return validation_uid.equals(data_uid);
    }
}
