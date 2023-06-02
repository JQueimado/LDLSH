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

public class ExternalIndexInsertServerTask extends MultimapTaskImpl {

    private final StorageMap storageMap;

    public ExternalIndexInsertServerTask(Message message, DataContainer appContext) throws Exception {
        super(message, appContext);
        storageMap = TraditionalAux.getStorageMap(appContext);
    }

    @Override
    public Message call() throws Exception {

        //assert type
        if( message.getType() != Message.types.EXTERNAL_INDEX_INSERT )
            throw new InvalidMessageTypeException(Message.types.EXTERNAL_INDEX_INSERT, message.getType());

        //assert body size
        if ( message.getBody().size() != 2 )
            throw new InvalidMessageSizeException( 2, message.getBody().size() );

        //assert types
        Object temp;
        UniqueIdentifier key;
        DataObject<?> value;
        temp = message.getBody().get(0);

        if( !(temp instanceof UniqueIdentifier) )
            throw new InvalidMessageBodyObjectException(
                    UniqueIdentifier.class.toString(),
                    temp.getClass().toString()
            );

        key = (UniqueIdentifier) temp;
        temp = message.getBody().get(1);

        if( !(temp instanceof DataObject<?>) )
            throw new InvalidMessageBodyObjectException(
                    DataObject.class.toString(),
                    temp.getClass().toString()
            );

        value = (DataObject<?>) temp;

        //Insert Value
        storageMap.insert(key, value);

        List<Object> body = new ArrayList<>();
        body.add(true);
        return new MessageImpl(Message.types.INSERT_MESSAGE_RESPONSE, body);
    }
}
