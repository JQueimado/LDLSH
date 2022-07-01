package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.CommunicationLayer;
import NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;
import com.google.common.util.concurrent.ListenableFutureTask;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

public class NettyCommunicationLayer implements CommunicationLayer {

    private static final String hasClient_config = "HAS_CLIENT";
    private static final String hasServer_config = "HAS_SERVER";

    Bootstrap clientBootstrap;
    NettyReceiver nettyReceiver;
    EventLoopGroup eventExecutors;
    SynchronousQueue<Promise<Message>> client_queue;

    HashMap<String, ChannelFuture> connections;

    public NettyCommunicationLayer( DataContainer appContext ) throws UnknownConfigException {
        //Client
        String hasClient_string = "";
        try {
            hasClient_string = appContext.getConfigurator().getConfig(hasClient_config);
            boolean hasClient = Boolean.parseBoolean(hasClient_string);
            if (hasClient) {
                client_queue = new SynchronousQueue<>();
                eventExecutors = new NioEventLoopGroup();
                clientBootstrap = new Bootstrap();
                clientBootstrap.group(eventExecutors);
                clientBootstrap.channel(NioSocketChannel.class);
                clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new NettyMessageEncoder(),
                                new NettyMessageDecoder(),
                                new NettyClientHandler(client_queue)
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
            boolean hasServer = Boolean.parseBoolean(hasServer_string);
            if( hasServer ) {
                nettyReceiver = new NettyReceiver(appContext);
                nettyReceiver.run();
            }
        }catch ( Exception e){
            throw new UnknownConfigException( hasServer_config,  hasServer_string);
        }
    }

    @Override
    public Promise<Message> send(Message message, String hostname, int port) throws Exception {
        String connectionName = hostname + ":" + port;
        ChannelFuture channelFuture;
        if( ( channelFuture = connections.get(connectionName) ) == null ) {
            channelFuture = clientBootstrap.connect(hostname, port).sync();
            connections.put( connectionName, channelFuture );
            channelFuture.await();
        }
        Channel channel = channelFuture.channel();

        Promise<Message> promise = channel.eventLoop().newPromise();
        client_queue.offer(promise);

        channel.writeAndFlush(message).addListener( future -> {
            if( future.isDone() )
                System.out.println("Message Sent to: "+ channel.remoteAddress());
            else
                System.out.println("Error sending message to: " + channel.remoteAddress());
        }).get();

        return promise;
    }

    //Receiver | Server side
    public class NettyReceiver implements CommunicationLayer.Receiver{

        private static final String server_port_config = "SERVER_PORT";

        private final DataContainer appContext;
        private int server_port;

        public NettyReceiver(DataContainer context) throws UnknownConfigException {
            this.appContext = context;
            setup();
        }

        private void setup() throws UnknownConfigException {
            String server_port_string_value = "";
            try {
                server_port_string_value = appContext.getConfigurator().getConfig(server_port_config);
                this.server_port = Integer.parseInt(server_port_string_value);
            }catch (Exception e){
                throw new UnknownConfigException(server_port_config, server_port_string_value);
            }
        }

        @Override
        public void run() throws Exception {
            EventLoopGroup masterGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group( masterGroup, workerGroup )
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(
                                        new NettyMessageDecoder(),
                                        new NettyMessageEncoder(),
                                        new NettyServerHandler());
                            }
                        }).option(ChannelOption.SO_BACKLOG, 128 )
                        .childOption( ChannelOption.SO_KEEPALIVE, true );

                ChannelFuture f = b.bind(server_port).sync();
                System.out.println( "Server opened at port " + server_port );
                f.channel().closeFuture().sync();
            }catch (Exception e){
                throw new NettyServerException(e.getMessage());
            } finally {
                workerGroup.shutdownGracefully();
                masterGroup.shutdownGracefully();
            }
        }
    }

}
