package SystemLayer.Components.AdditionalStructures.StorageMap;

import SystemLayer.Components.NetworkLayer.CommunicationLayer;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.NetworkLayer.MessageImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.InvalidMessageBodyObjectException;
import SystemLayer.SystemExceptions.InvalidMessageSizeException;
import SystemLayer.SystemExceptions.InvalidMessageTypeException;
import io.netty.util.concurrent.Promise;

import java.util.ArrayList;
import java.util.List;

public class RemoteStorageMap extends StorageMapImpl {

    private final String storageMapHost_config = "STORAGE_MAP_ENDPOINT";

    private final String host;
    private final int port;
    private final CommunicationLayer communicationLayer;

    public RemoteStorageMap(DataContainer appContext) {
        super(appContext);
        communicationLayer = appContext.getCommunicationLayer();

        String storageMapHost_value = appContext.getConfigurator().getConfig(storageMapHost_config);
        String[] values = storageMapHost_value.split(":");
        host = values[0];
        port = Integer.parseInt(values[1]);
    }

    @Override
    public void insert(UniqueIdentifier key, DataObject<?> value) throws Exception {
        List<Object> messageBody = new ArrayList<>();
        messageBody.add(key);
        messageBody.add(value);
        Message message = new MessageImpl(Message.types.INSERT_MESSAGE, messageBody);

        Promise<Message> result_future = communicationLayer.send(message, host, port);

        result_future.addListener(future -> {
            Message result = result_future.get();

            if(result.getType() != Message.types.INSERT_MESSAGE_RESPONSE)
                throw new InvalidMessageTypeException(Message.types.INSERT_MESSAGE_RESPONSE, result.getType());

            if (result.getBody().size() != 1)
                throw new InvalidMessageSizeException(1, result.getBody().size());

            Object result_body = result.getBody().get(0);
            if( !(result_body instanceof Boolean) )
                throw new InvalidMessageBodyObjectException(
                        Boolean.class.toString(),
                        result_body.getClass().toString()
                );

            boolean bool = (Boolean) result_body;

            if( bool ) {
                //All good
            }else {
                throw new Exception("ERROR: server side Insertion error");
            }
        });
    }

    @Override
    public DataObject<?> query(UniqueIdentifier key) throws Exception {
        List<Object> messageBody = new ArrayList<>();
        messageBody.add(key);
        Message message = new MessageImpl(Message.types.QUERY_MESSAGE, messageBody );

        Promise<Message> result_future = communicationLayer.send(message, host, port);
        Message result = result_future.get();

        if( result.getType() != Message.types.QUERY_RESPONSE )
            throw new InvalidMessageTypeException(Message.types.QUERY_RESPONSE, result.getType());

        if( result.getBody().size() != 1 )
            throw new InvalidMessageSizeException(1, result.getBody().size());

        Object object = result.getBody().get(0);
        if ( !(object instanceof DataObject<?>) && object != null )
            throw new InvalidMessageBodyObjectException(DataObject.class.toString(), object.getClass().toString());

        return (DataObject<?>) object;
    }
}
