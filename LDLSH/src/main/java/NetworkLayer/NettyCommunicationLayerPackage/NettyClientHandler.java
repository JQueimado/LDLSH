package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final ConcurrentHashMap<Integer, Promise<Message>> transactionMap;

    public NettyClientHandler( ConcurrentHashMap<Integer, Promise<Message>> transactionMap ){
        super();
        this.transactionMap = transactionMap;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Handler added for" + ctx.channel().localAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Handler removed for "  + ctx.channel().localAddress());
    }

    //Server Responses
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message response = (Message) msg;
        int transactionId = response.getTransactionId();
        Promise<Message> responsePromise = transactionMap.get(transactionId);
        transactionMap.remove( transactionId );
        responsePromise.setSuccess( response );
    }
}
