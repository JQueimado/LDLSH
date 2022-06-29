package SystemLayer.Components.TaskImpl.Multimap;

import NetworkLayer.Message;
import NetworkLayer.MessageImpl;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodes;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

public class InsertMultimapTask implements MultimapTask<Boolean> {

    private LSHHash hash;
    private UniqueIdentifier uid;
    private ErasureBlock block;
    private DataContainer appContext;

    public InsertMultimapTask( Message insertMessage, DataContainer appContext ) throws Exception {
        if( insertMessage.getBody().size() != 3 )
            throw new Exception("Invalid body Size for message type: INSERT_MESSAGE");
        this.hash = (LSHHash) insertMessage.getBody().get(0);
        this.uid = (UniqueIdentifier) insertMessage.getBody().get(1);
        this.block = (ErasureBlock) insertMessage.getBody().get(2);
        this.appContext = appContext;
    }

    @Override
    public Boolean call() {
        try {
            MultiMap[] multiMaps = appContext.getMultiMaps();
            for ( MultiMap multiMap : multiMaps ){
                multiMap.insert(hash, uid, block);
            }
        } catch (Exception e) {
            e.printStackTrace();
            appContext.getCommunicationLayer();
        }
        return true;
    }
}
