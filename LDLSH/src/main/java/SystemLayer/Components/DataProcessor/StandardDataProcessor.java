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
import SystemLayer.SystemExceptions.IncompleteBlockException;

public class StandardDataProcessor extends DataProcessorImpl{

    public StandardDataProcessor(DataContainer appContext) {
        super(appContext);
    }

    @Override
    public ProcessedData preProcessData(DataObject object) throws Exception {
        int numberOfBlocks = appContext.getNumberOfBands();

        //LSH hash
        LSHHash object_hash = preprocessLSH(object);

        //Erasure codes
        ErasureCodes object_erasureCodes = appContext.getErasureCodesFactory().getNewErasureCodes();
        object_erasureCodes.encodeDataObject( object.toByteArray(), numberOfBlocks );

        //UID
        UniqueIdentifier object_uniqueIdentifier = appContext.getUniqueIdentifierFactory().getNewUniqueIdentifier();
        object_uniqueIdentifier.setObject( object.toByteArray() );

        return new ProcessedData(object, object_hash, object_uniqueIdentifier, object_erasureCodes);
    }

    @Override
    public DataObject postProcess( ErasureCodes erasureCodes, UniqueIdentifier uniqueIdentifier )
            throws CorruptDataException, IncompleteBlockException {

        DataObject dataObject = appContext.getDataObjectFactory().getNewDataObject();
        dataObject.setByteArray( erasureCodes.decodeDataObject() );

        if( !validate( dataObject.toByteArray(), uniqueIdentifier ) )
            throw new CorruptDataException(
                    "An error occurred while reconstructing the data object",
                    erasureCodes,
                    uniqueIdentifier);

        return dataObject;
    }

    @Override
    public LSHHash preprocessLSH(DataObject object) {
        LSHHash object_hash = appContext.getLshHashFactory().getNewLSHHash(); //Gets based on config file
        object_hash.setObject(object.toByteArray(),appContext.getNumberOfBands());

        return object_hash;
    }
}
