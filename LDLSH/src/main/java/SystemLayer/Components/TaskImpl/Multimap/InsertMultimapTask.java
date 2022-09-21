package SystemLayer.Components.TaskImpl.Multimap;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.NetworkLayer.MessageImpl;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.LSHHashImpl.LSHHash;

import java.util.ArrayList;
import java.util.List;

public class InsertMultimapTask extends MultimapTaskImpl {

    public InsertMultimapTask( Message insertMessage, DataContainer appContext ){
        super( insertMessage, appContext );
    }

    @Override
    public Message call() throws Exception {
        if( message.getBody().size() != 2 )
            throw new Exception("Invalid body Size for message type: INSERT_MESSAGE");

        LSHHash hash = (LSHHash) message.getBody().get(0);
        MultiMapValue value = (MultiMapValue) message.getBody().get(1);

        List<Object> responseBody = new ArrayList<>();
        try {
            List<MultiMap> multiMaps = appContext.getMultiMaps();
            for ( MultiMap multiMap : multiMaps ){
                multiMap.insert(hash, value);
            }
            responseBody.add(true);
        } catch (Exception e) {
            e.printStackTrace();
            appContext.getCommunicationLayer();
            responseBody.add(false);
        }

        return new MessageImpl(Message.types.INSERT_MESSAGE_RESPONSE, responseBody);
    }
}
