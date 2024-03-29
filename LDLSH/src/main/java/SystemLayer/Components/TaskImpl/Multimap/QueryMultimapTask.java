package SystemLayer.Components.TaskImpl.Multimap;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.NetworkLayer.MessageImpl;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.DataUnits.LSHHashBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryMultimapTask extends MultimapTaskImpl {

    public QueryMultimapTask( Message queryMessage, DataContainer appContext ){
        super(queryMessage, appContext);
    }

    @Override
    public Message call() throws Exception {
        //Evaluate message body
        if( message.getBody().size() != 1 ){
            throw new Exception("Invalid body Size for message type: QUERY_MESSAGE_SINGLE_BLOCK");
        }
        LSHHashBlock block = (LSHHashBlock) message.getBody().get(0); //fetch body

        List<MultiMap> multiMaps = appContext.getMultiMaps();
        List<MultiMapValue> results = new ArrayList<>();
        for( MultiMap multiMap : multiMaps ) {
            MultiMapValue[] temp = multiMap.query(block);
            Collections.addAll(results, temp);
        }
        List<Object> responseBody = new ArrayList<>( results );
        return new MessageImpl(Message.types.QUERY_RESPONSE, responseBody);
    }
}
