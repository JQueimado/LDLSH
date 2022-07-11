package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final ConcurrentHashMap<Integer, Promise<Message>> transactionMap;
    private ByteBuf temp;

    public NettyClientHandler( ConcurrentHashMap<Integer, Promise<Message>> transactionMap ){
        super();
        this.transactionMap = transactionMap;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Handler added for" + ctx.channel().remoteAddress());
        temp = ctx.alloc().directBuffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Handler removed for "  + ctx.channel().remoteAddress());
        temp.release();
        temp = null;
    }

    //Server Responses
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //System.out.println("Received " + ((ByteBuf) msg).readableBytes() + "bytes");
        temp.writeBytes((ByteBuf) msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        //System.out.println("Read Complete");

        //Decode
        byte[] body = new byte[temp.readableBytes()];
        temp.readBytes(body);

        Message response = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(body);
            ObjectInputStream ois = new ObjectInputStream(bis);

            //Process Message
             response = (Message) ois.readObject();
        }catch (EOFException e){
            //System.out.println("Decode attempt failed: Stream wasn't complete");
            temp.writeBytes(body);
            return;
        }
        System.out.println( "Received "+response.getType()
                +" message from "+ctx.channel().remoteAddress()
                +" of size: "+temp.writerIndex()
        );
        temp.release();
        temp = ctx.alloc().directBuffer();

        int transactionId = response.getTransactionId();
        Promise<Message> responsePromise = transactionMap.get(transactionId);
        transactionMap.remove( transactionId );
        responsePromise.setSuccess( response );
    }
}
