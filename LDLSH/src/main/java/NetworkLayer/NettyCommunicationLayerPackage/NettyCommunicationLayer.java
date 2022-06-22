package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.CommunicationLayer;
import NetworkLayer.Message;
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

public class NettyCommunicationLayer implements CommunicationLayer {

    @Override
    public void broadcast(Message message) {

    }

    @Override
    public void send(Message message) {

    }

    public class NettyReceiver implements CommunicationLayer.Receiver{

        private static final String server_port_config = "SERVER_PORT";

        private final DataContainer appContext;
        private int server_port;

        public NettyReceiver(DataContainer context){
            this.appContext = context;
            try {
                setup();
            }catch (UnknownConfigException uce){
                UnknownConfigException.handler(uce);
            }
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
                                        new NettyProcessingHandler());
                            }
                        }).option(ChannelOption.SO_BACKLOG, 128 )
                        .childOption( ChannelOption.SO_KEEPALIVE, true );

                ChannelFuture f = b.bind(server_port).sync();
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
