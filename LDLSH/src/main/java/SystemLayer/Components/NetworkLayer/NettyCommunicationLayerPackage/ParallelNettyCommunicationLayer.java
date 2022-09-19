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

public class ParallelNettyCommunicationLayer implements CommunicationLayer {

    private static final String hasClient_config = "HAS_CLIENT";
    private static final String hasServer_config = "HAS_SERVER";

    private final boolean hasClient;
    private final boolean hasServer;

    private final DataContainer appContext;
    private Bootstrap clientBootstrap;
    private NettyReceiver nettyReceiver;
    private ConcurrentHashMap<Integer, Promise<Message>> transactionMap;
    private final AtomicInteger transactionIdGenerator = new AtomicInteger();

    private List<Channel> connections;

    public ParallelNettyCommunicationLayer(DataContainer appContext ) throws Exception {
        this.appContext = appContext;
        //Client
        String hasClient_string = "";
        try {
            hasClient_string = appContext.getConfigurator().getConfig(hasClient_config);
            hasClient = Boolean.parseBoolean(hasClient_string);
            if (hasClient) {
                transactionMap = new ConcurrentHashMap<>();
                EventLoopGroup eventExecutors = new NioEventLoopGroup();
                clientBootstrap = new Bootstrap();
                clientBootstrap.group(eventExecutors);
                clientBootstrap.channel(NioSocketChannel.class);
                clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new NettyMessageEncoder( appContext ),
                                new NettyClientHandler(transactionMap, appContext)
                        );
                    }
                });

                connections = new LinkedList<>();

            }
        }catch (IllegalArgumentException e){
            throw new UnknownConfigException( hasClient_config,  hasClient_string);
        }

        //Server Side
        String hasServer_string = "";
        try{
            hasServer_string = appContext.getConfigurator().getConfig(hasServer_config);
            hasServer = Boolean.parseBoolean(hasServer_string);
            if( hasServer ) {
                nettyReceiver = new NettyReceiver(appContext);
                nettyReceiver.run();
            }
        }catch ( IllegalArgumentException e){
            throw new UnknownConfigException( hasServer_config,  hasServer_string);
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

        connections.add(channel);

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
        connections.remove(channel);

        return promise; //Returns the response promise
    }

    @Override
    public void shutdown() {
        if(hasClient)
            for (Channel channel : connections){
                channel.close();
            }
        if(hasServer)
            nettyReceiver.shutdown();
    }

}
