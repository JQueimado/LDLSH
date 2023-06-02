package SystemLayer.Components.TaskImpl.Multimap.ExternalIndexServerTasks;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.NetworkLayer.MessageImpl;
import SystemLayer.Components.AdditionalStructures.StorageMap.StorageMap;
import SystemLayer.Components.TaskImpl.Multimap.MultimapTaskImpl;
import SystemLayer.Components.TaskImpl.TraditionalAux.TraditionalAux;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.InvalidMessageBodyObjectException;
import SystemLayer.SystemExceptions.InvalidMessageSizeException;
import SystemLayer.SystemExceptions.InvalidMessageTypeException;

import java.util.ArrayList;
import java.util.List;

public class ExternalIndexQueryServerTask extends MultimapTaskImpl {

    private final StorageMap storageMap;

    public ExternalIndexQueryServerTask(Message message, DataContainer appContext) throws Exception {
        super(message, appContext);
        storageMap = TraditionalAux.getStorageMap(appContext);
    }

    @Override
    public Message call() throws Exception {

        //assert type
        if( message.getType() != Message.types.EXTERNAL_INDEX_QUERY )
            throw new InvalidMessageTypeException(Message.types.EXTERNAL_INDEX_QUERY, message.getType());

        //assert body size
        if ( message.getBody().size() != 1 )
            throw new InvalidMessageSizeException( 1, message.getBody().size() );

        //assert types
        Object temp;
        UniqueIdentifier key;
        temp = message.getBody().get(0);

        if( !(temp instanceof UniqueIdentifier) )
            throw new InvalidMessageBodyObjectException(
                    UniqueIdentifier.class.toString(),
                    temp.getClass().toString()
            );

        key = (UniqueIdentifier) temp;

        //Insert Value
        DataObject<?> result = storageMap.query(key);
        List<Object> body = new ArrayList<>();
        body.add(result);
        return new MessageImpl( Message.types.QUERY_RESPONSE, body);
    }
}
