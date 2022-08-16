package SystemLayer.Components.NetworkLayer.NettyCommunicationLayerPackage;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final DataContainer appContext;
    private final ConcurrentHashMap<Integer, Promise<Message>> transactionMap;
    private ByteBuf temp;

    public NettyClientHandler( ConcurrentHashMap<Integer, Promise<Message>> transactionMap, DataContainer appContext ){
        super();
        this.transactionMap = transactionMap;
        this.appContext = appContext;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        if( appContext.getDebug() )
            System.out.println("NettyClientHandler: Handler added for" + ctx.channel().localAddress());
        temp = ctx.alloc().directBuffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if( appContext.getDebug() )
            System.out.println("NettyClientHandler: Handler removed for "  + ctx.channel().localAddress());
        if( temp.release() )
            temp = null;
    }

    //Server Responses
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if( appContext.getDebug() )
            System.out.println("NettyClientHandler: Received " + ((ByteBuf) msg).readableBytes() + "bytes");
        temp.writeBytes((ByteBuf) msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);

        if( appContext.getDebug() )
            System.out.println("NettyClientHandler: Read Complete");

        while (temp.readableBytes()>0) {
            //Decode
            byte[] body = new byte[temp.readableBytes()];
            temp.readBytes(body);

            Message response = null;
            try {
                if (appContext.getDebug())
                    System.out.println("NettyClientHandler: Start Buffer");
                ByteArrayInputStream bis = new ByteArrayInputStream(body);
                ObjectInputStream ois = new ObjectInputStream(bis);

                //Process Message
                if (appContext.getDebug())
                    System.out.println("NettyClientHandler: Read Buffer");
                response = (Message) ois.readObject();

                temp.writeBytes( bis.readAllBytes() );

                if (appContext.getDebug())
                    System.out.println("NettyClientHandler: Remaining Bytes to process: " + temp.readableBytes());

            } catch (EOFException e) {
                //System.out.println("Decode attempt failed: Stream wasn't complete");
                if (appContext.getDebug())
                    System.out.println("NettyClientHandler: Read Failed resetting message buffer");
                temp.writeBytes(body);
                return;
            }

            if (appContext.getDebug())
                System.out.println("NettyClientHandler: Received " + response.getType()
                        + " message from " + ctx.channel().remoteAddress()
                );

            int transactionId = response.getTransactionId();
            Promise<Message> responsePromise = transactionMap.get(transactionId);
            transactionMap.remove(transactionId);
            responsePromise.setSuccess(response);
        }
    }
}
