package SystemLayer.Components.TaskImpl.Multimap;

import NetworkLayer.Message;
import NetworkLayer.MessageImpl;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.LSHHashImpl.LSHHashImpl;
import SystemLayer.Data.LSHHashImpl.LSHHashImpl.LSHHashBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryMultimapTask implements MultimapTask {
    private final DataContainer appContext;
    private final Message queryMessage;

    public QueryMultimapTask( Message queryMessage, DataContainer appContext ){
        this.queryMessage = queryMessage;
        this.appContext = appContext;
    }

    @Override
    public Message call() throws Exception {
        //Evaluate message body
        if( queryMessage.getBody().size() != 1 ){
            throw new Exception("Invalid body Size for message type: QUERY_MESSAGE_SINGLE_BLOCK");
        }
        LSHHashBlock block = (LSHHashBlock) queryMessage.getBody().get(0); //fetch body

        MultiMap[] multiMaps = appContext.getMultiMaps();
        List<MultiMapValue> results = new ArrayList<>();
        for( MultiMap multiMap : multiMaps ) {
            MultiMapValue[] temp = multiMap.query(block);
            Collections.addAll(results, temp);
        }
        List<Object> responseBody = new ArrayList<>( results );
        return new MessageImpl(Message.types.QUERY_RESPONSE, responseBody);
    }
}
