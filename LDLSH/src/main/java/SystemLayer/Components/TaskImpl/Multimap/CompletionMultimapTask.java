package SystemLayer.Components.TaskImpl.Multimap;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.NetworkLayer.MessageImpl;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.util.ArrayList;
import java.util.List;

import static SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.*;

public class CompletionMultimapTask extends MultimapTaskImpl {

    public CompletionMultimapTask( Message completion_message, DataContainer appContext ){
        super(completion_message, appContext);
    }

    @Override
    public Message call() throws Exception {
        if (message.getBody().size() != 2)
            throw new Exception("Invalid body Size for message type: COMPLETION_MESSAGE");

        LSHHash hash = (LSHHash) message.getBody().get(0);
        UniqueIdentifier uid = (UniqueIdentifier) message.getBody().get(1);

        MultiMap[] multiMaps = appContext.getMultiMaps();
        List<ErasureBlock> results = new ArrayList<>();
        for (MultiMap multiMap : multiMaps) {
            ErasureBlock block = multiMap.complete(hash, uid);
            if (block != null)
                results.add(block);
        }
        List<Object> responseBody = new ArrayList<>(results);
        return new MessageImpl(Message.types.COMPLETION_RESPONSE, responseBody);
    }
}
