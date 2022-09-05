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
    private static final String msg_timeout_config = "MESSAGE_TIMEOUT";

    //Remote
    private final DataContainer appContext;
    private String host = "";
    private int port = 0;
    private final CommunicationLayer communicationLayer;
    private int TIMEOUT;

    public RemoteMultimap( DataContainer appContext ){
        super(appContext);
        this.appContext = appContext;
        this.communicationLayer = appContext.getCommunicationLayer();

        String msg_timeout_value = "";
        try{
            msg_timeout_value = appContext.getConfigurator().getConfig(msg_timeout_config);
            TIMEOUT = Integer.parseInt(msg_timeout_value);
        }catch (Exception e){
            UnknownConfigException.handler(new UnknownConfigException(msg_timeout_config, msg_timeout_value));
        }

    }

    @Override
    public boolean insert(LSHHash lshHash, MultiMapValue value) throws Exception {
        List<Object> messageBody = new ArrayList<>();
        messageBody.add(lshHash);
        messageBody.add(value);
        Message insertMessage = new MessageImpl(Message.types.INSERT_MESSAGE, messageBody);

        Promise<Message> responsePromise;
        while(true) {
            responsePromise = communicationLayer.send(insertMessage, host, port);
            if (!responsePromise.await(TIMEOUT, TimeUnit.SECONDS))
                System.err.println("RemoteMultimap: Message timeout resending.");
            else
                break;
        }

        Message response = responsePromise.get();

        try {
            //Message type
            if (response.getType() != Message.types.INSERT_MESSAGE_RESPONSE) {
                System.err.println("Invalid response type: " + response.getType());
                return false;
            }

            if (response.getBody().size() == 1) {
                //if (appContext.getDebug())
                //    System.out.println("Insert Complete");
                return true;
            } else {
                System.err.println( "Remote error:\n" +
                        (String) response.getBody().get(0) + "\n" +
                        (String) response.getBody().get(1)
                );
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ErasureBlock complete(LSHHash lshHash, UniqueIdentifier uniqueIdentifier) throws Exception {
        List<Object> messageBody = new ArrayList<>();
        messageBody.add(lshHash);
        messageBody.add(uniqueIdentifier);
        Message completionMessage = new MessageImpl(Message.types.COMPLETION_MESSAGE, messageBody);
        Promise<Message> responsePromise;
        while(true) {
            responsePromise = communicationLayer.send(completionMessage, host, port);
            if (!responsePromise.await(TIMEOUT, TimeUnit.SECONDS))
                System.err.println("RemoteMultimap: Message timeout resending.");
            else
                break;
        }

        Message response = responsePromise.get();

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

        Promise<Message> responsePromise;
        while(true) {
            responsePromise = communicationLayer.send(queryMessage, host, port);
            if (!responsePromise.await(TIMEOUT, TimeUnit.SECONDS))
                System.err.println("RemoteMultimap: Message timeout in host "+host+":"+port+" resending.");
            else
                break;
        }

        Message response = responsePromise.get();

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
