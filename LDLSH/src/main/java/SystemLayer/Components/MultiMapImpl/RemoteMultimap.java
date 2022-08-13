package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Components.NetworkLayer.CommunicationLayer;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.NetworkLayer.MessageImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.DataUnits.LSHHashBlock;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.UnknownConfigException;
import io.netty.util.concurrent.Promise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RemoteMultimap extends MultiMapImpl{

    private static final String endpoint_config = "MULTIMAP_ENDPOINTS";
    private static final int TIMEOUT = 1;

    //Remote
    private final DataContainer appContext;
    private String host = "";
    private int port = 0;
    private final CommunicationLayer communicationLayer;

    public RemoteMultimap( DataContainer appContext ){
        super(appContext);
        this.appContext = appContext;
        this.communicationLayer = appContext.getCommunicationLayer();
    }

    @Override
    public void insert(LSHHash lshHash, MultiMapValue value) throws Exception {
        List<Object> messageBody = new ArrayList<>();
        messageBody.add(lshHash);
        messageBody.add(value);
        Message insertMessage = new MessageImpl(Message.types.INSERT_MESSAGE, messageBody);
        Promise<Message> responsePromise;
        synchronized (this) {
            responsePromise = communicationLayer.send(insertMessage, host, port);
        }

        responsePromise.addListener(responseFuture -> {
            try {
                if (responseFuture.isSuccess()) {
                    Message response = (Message) responseFuture.get();
                    //Message type
                    if (response.getType() != Message.types.INSERT_MESSAGE_RESPONSE) {
                        System.err.println("Invalid response type: " + response.getType());
                    }

                    if (response.getBody().size() == 1) {
                        if (appContext.getDebug())
                            System.out.println("Insert Complete");
                    } else {
                        System.err.println( "Remote error:\n" +
                                (String) response.getBody().get(0) + "\n" +
                                (String) response.getBody().get(1)
                        );
                    }
                } else {
                    System.err.println( "Unknown Server Error" );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public ErasureBlock complete(LSHHash lshHash, UniqueIdentifier uniqueIdentifier) throws Exception {
        List<Object> messageBody = new ArrayList<>();
        messageBody.add(lshHash);
        messageBody.add(uniqueIdentifier);
        Message completionMessage = new MessageImpl(Message.types.COMPLETION_MESSAGE, messageBody);
        Promise<Message> response_promise;
        synchronized (this){
            response_promise = communicationLayer.send(completionMessage, host, port);
        }
        if (!response_promise.await(TIMEOUT, TimeUnit.SECONDS) )
            throw new TimeoutException();

        Message response = response_promise.get();

        if( response.getType() != Message.types.COMPLETION_RESPONSE ){
            throw new Exception( "ERROR: Invalid response format" );
        }
        ErasureBlock result = (ErasureBlock) response.getBody().get(0);
        if( result == null )
            throw new Exception( "ERROR: Remote Operation failed" );
        return result;
    }

    @Override
    public MultiMapValue[] query(LSHHashBlock lshHash) throws Exception {
        List<Object> messageBody = new ArrayList<>();
        messageBody.add(lshHash);
        Message queryMessage = new MessageImpl(Message.types.QUERY_MESSAGE_SINGLE_BLOCK, messageBody);
        Promise<Message> response_promise;
        synchronized (this) {
            response_promise = communicationLayer.send(queryMessage, host, port);
        }
        if( response_promise.await(TIMEOUT, TimeUnit.SECONDS) )
            throw new TimeoutException();

        Message response = response_promise.get();

        if( response.getType() != Message.types.QUERY_RESPONSE ){
            throw new Exception( "ERROR: Invalid response format" );
        }

        Object[] rawValues = response.getBody().toArray();
        MultiMapValue[] values = new MultiMapValue[rawValues.length];
        System.arraycopy(rawValues, 0, values, 0, rawValues.length);
        return values;
    }

    @Override
    public void setHashBlockPosition(int position) {
        super.setHashBlockPosition(position); //Set value
        String mapEndpoints = "";
        try{
            //Get endpoint
            mapEndpoints = appContext.getConfigurator().getConfig(endpoint_config);
            String[] endpoints = mapEndpoints.split(";");
            String endpoint = endpoints[hash_position];

            //Split endpoint
            String[] hostPortSplit = endpoint.split(":");
            this.host = hostPortSplit[0];
            this.port = Integer.parseInt( hostPortSplit[1] );

        }catch (IllegalArgumentException e){
            UnknownConfigException.handler( new UnknownConfigException( endpoint_config, mapEndpoints ) );
        }

    }
}
