package SystemLayer.Components.DataProcessor;

import Factories.DataFactories.ErasureCodesFactory;
import Factories.DataFactories.LSHHashFactory;
import Factories.DataFactories.UniqueIdentifierFactory;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.CorruptDataException;

public class StandardDataProcessor extends DataProcessorImpl{

    private final String LSH_config;
    private final String Erasure_config;
    private final String UID_config;

    public StandardDataProcessor(DataContainer appContext) {
        super(appContext);
        this.LSH_config = appContext.getConfigurator().getConfig(LSHHashFactory.config_name );
        this.Erasure_config = appContext.getConfigurator().getConfig(ErasureCodesFactory.config_name);
        this.UID_config = appContext.getConfigurator().getConfig(UniqueIdentifierFactory.config_name);
    }

    @Override
    public ProcessedData preProcessData(DataObject object){
        int numberOfBlocks = appContext.getNumberOfBands();

        //LSH hash
        LSHHash object_hash = appContext.getLshHashFactory().getNewLSHHash(LSH_config); //Gets based on config file
        object_hash.setObject(object.toByteArray(),numberOfBlocks);

        //Erasure codes
        ErasureCodes object_erasureCodes = appContext.getErasureCodesFactory().getNewErasureCodes(Erasure_config);
        object_erasureCodes.encodeDataObject( object.toByteArray(), numberOfBlocks );

        //UID
        UniqueIdentifier object_uniqueIdentifier = appContext.getUniqueIdentifierFactory()
                .getNewUniqueIdentifier(UID_config);
        object_uniqueIdentifier.setObject( object.toByteArray() );

        return new ProcessedData(object, object_hash, object_uniqueIdentifier, object_erasureCodes);
    }

    @Override
    public DataObject postProcess( ErasureCodes erasureCodes, UniqueIdentifier uniqueIdentifier ) throws Exception {
        DataObject dataObject = appContext.getDataObjectFactory().getNewDataObject();
        dataObject.setByteArray( erasureCodes.decodeDataObject() );

        if( !validate( dataObject.toByteArray(), uniqueIdentifier ) )
            throw new CorruptDataException(
                    "An error occurred while reconstructing the data object",
                    erasureCodes,
                    uniqueIdentifier);

        return dataObject;
    }
}
