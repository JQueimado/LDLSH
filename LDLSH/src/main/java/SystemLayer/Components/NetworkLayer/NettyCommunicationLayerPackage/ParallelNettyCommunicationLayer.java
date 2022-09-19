package SystemLayer.Components.NetworkLayer.NettyCommunicationLayerPackage;

import SystemLayer.Components.NetworkLayer.CommunicationLayer;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelNettyCommunicationLayer extends NettyCommunicationLayer {

    private List<Channel> connectionsList;

    public ParallelNettyCommunicationLayer(DataContainer appContext ) throws Exception {
        super(appContext);
        if( hasClient ) {
            connectionsList = new LinkedList<>();
            connections = null;
        }
    }

    @Override
    public Promise<Message> send(Message message, String hostname, int port) throws Exception {
        if( ! hasClient )
            throw new Exception("Node has not initialized a client");

        Channel channel;
        ChannelFuture channelFuture = clientBootstrap.connect(hostname, port).sync();
        channelFuture.await();
        channel = channelFuture.channel();

        connectionsList.add(channel);

        Promise<Message> promise = channel.eventLoop().newPromise(); //Creates a response promise

        //Creates a unique transaction id
        int transactionId;
        synchronized ( transactionIdGenerator ){
            transactionId = transactionIdGenerator.getAndIncrement();
        }

        message.setTransactionId(transactionId); //Sets the message transaction id
        transactionMap.put( transactionId, promise ); //Adds transaction to the map

        channel.writeAndFlush(message); //Sends message to server

        //if(appContext.getDebug())
        //    System.out.println("NettyCommunicationLayer: Sent " +
        //            message.getType().toString() + " to " +connectionName);

        promise.get(); //Await response
        channel.close();
        connectionsList.remove(channel);

        return promise; //Returns the response promise
    }

    @Override
    public void shutdown() {
        if(hasClient)
            for (Channel channel : connectionsList){
                channel.close();
            }
        if(hasServer)
            nettyReceiver.shutdown();
    }

}
