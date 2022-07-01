package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.SynchronousQueue;

public class NettyClientHandler extends SimpleChannelInboundHandler<Message> {

    private final SynchronousQueue<Promise<Message>> queue;

    public NettyClientHandler( SynchronousQueue<Promise<Message>> queue ){
        super();
        this.queue = queue;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Handler added for" + ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Handler removed for "  + ctx.channel().remoteAddress());
    }

    //Server Responses
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        this.queue.remove().setSuccess(msg);
    }
}
