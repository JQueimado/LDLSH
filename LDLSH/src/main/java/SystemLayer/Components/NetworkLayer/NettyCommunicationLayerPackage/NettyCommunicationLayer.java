package SystemLayer.Components.NetworkLayer.NettyCommunicationLayerPackage;

import SystemLayer.Components.NetworkLayer.CommunicationLayer;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyCommunicationLayer implements CommunicationLayer {

    private static final String hasClient_config = "HAS_CLIENT";
    private static final String hasServer_config = "HAS_SERVER";

    private final boolean hasClient;
    private final boolean hasServer;

    private final DataContainer appContext;
    private Bootstrap clientBootstrap;
    private NettyReceiver nettyReceiver;
    private ConcurrentHashMap<Integer, Promise<Message>> transactionMap;
    private final AtomicInteger transactionIdGenerator = new AtomicInteger();
    private final Object createChannelLock = new Object();

    HashMap<String, Channel> connections;

    public NettyCommunicationLayer( DataContainer appContext ) throws Exception {
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

                connections = new HashMap<>();
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

        String connectionName = hostname + ":" + port;
        Channel channel;
        synchronized (createChannelLock) {
            if ((channel = connections.get(connectionName)) == null) {
                ChannelFuture channelFuture = clientBootstrap.connect(hostname, port).sync();
                channelFuture.await();
                channel = channelFuture.channel();
                connections.put(connectionName, channel);
            }
        }

        Promise<Message> promise = channel.eventLoop().newPromise(); //Creates a response promise

        //Creates a unique transaction id
        int transactionId;
        synchronized ( transactionIdGenerator ){
            transactionId = transactionIdGenerator.getAndIncrement();
        }

        message.setTransactionId(transactionId); //Sets the message transaction id
        transactionMap.put( transactionId, promise ); //Adds transaction to the map

        synchronized (channel) {
            channel.writeAndFlush(message); //Sends message to server
            //Thread.sleep(5);
        }

        //if(appContext.getDebug())
        //    System.out.println("NettyCommunicationLayer: Sent " +
        //            message.getType().toString() + " to " +connectionName);

        return promise; //Returns the response promise
    }

    @Override
    public void shutdown() {
        if(hasClient)
            for (Channel channel : connections.values()){
                channel.close();
            }
        if(hasServer)
            nettyReceiver.shutdown();
    }
}
