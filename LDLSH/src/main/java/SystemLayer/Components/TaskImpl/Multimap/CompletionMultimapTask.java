package SystemLayer.Components.TaskImpl.Multimap;

import NetworkLayer.Message;
import NetworkLayer.MessageImpl;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.util.ArrayList;
import java.util.List;

import static SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.*;

public class CompletionMultimapTask implements MultimapTask {

    private final Message completion_message;
    private final DataContainer appContext;

    public CompletionMultimapTask( Message completion_message, DataContainer appContext ){
        this.completion_message = completion_message;
        this.appContext = appContext;
    }

    @Override
    public Message call() throws Exception {
        if (completion_message.getBody().size() != 2)
            throw new Exception("Invalid body Size for message type: COMPLETION_MESSAGE");

        LSHHash hash = (LSHHash) completion_message.getBody().get(0);
        UniqueIdentifier uid = (UniqueIdentifier) completion_message.getBody().get(1);

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
