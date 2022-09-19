package SystemLayer.Components.NetworkLayer.NettyCommunicationLayerPackage;

import SystemLayer.Components.NetworkLayer.CommunicationLayer;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

//Receiver | Server side
public class NettyReceiver implements CommunicationLayer.Receiver {

    private static final String server_port_config = "SERVER_PORT";

    private final DataContainer appContext;
    private int server_port;
    private ChannelFuture channelFuture;

    public NettyReceiver(DataContainer context) throws UnknownConfigException {
        this.appContext = context;
        setup();
    }

    private void setup() throws UnknownConfigException {
        String server_port_string_value = "";
        try {
            server_port_string_value = appContext.getConfigurator().getConfig(server_port_config);
            this.server_port = Integer.parseInt(server_port_string_value);
        } catch (Exception e) {
            throw new UnknownConfigException(server_port_config, server_port_string_value);
        }
    }

    @Override
    public void run() throws Exception {
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(masterGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new NettyMessageEncoder(appContext),
                                    new NettyServerHandler(appContext));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            channelFuture = b.bind(server_port).sync();
            //System.out.println( "Server opened at port " + server_port );
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new NettyServerException(e.getMessage());
        } finally {
            workerGroup.shutdownGracefully();
            masterGroup.shutdownGracefully();
        }
    }

    public void shutdown() {
        try {
            channelFuture.sync();
        } catch (InterruptedException e) {
            //pass
        }
        channelFuture.channel().close();
        channelFuture.channel().parent().close();
    }
}
