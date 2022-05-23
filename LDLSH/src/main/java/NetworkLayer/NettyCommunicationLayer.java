package NetworkLayer;

import SystemLayer.Containers.DataContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyCommunicationLayer implements CommunicationLayer {

    @Override
    public void broadcast(Message message) {

    }

    @Override
    public void send(Message message) {

    }

    public class NettyReceiver implements CommunicationLayer.Receiver{

        DataContainer context;

        public NettyReceiver(DataContainer context){
            this.context = context;
        }

        @Override
        public void run() {

        }

        public class Handler extends ChannelInboundHandlerAdapter {
            private ByteBuf tmp;

            @Override
            public void handlerAdded(ChannelHandlerContext ctx) {
                tmp = ctx.alloc().buffer(4);
            }

            @Override
            public void handlerRemoved(ChannelHandlerContext ctx) {
                tmp.release();
                tmp = null;
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                ByteBuf m = (ByteBuf) msg;
                tmp.writeBytes(m);
                m.release();
                if (tmp.readableBytes() >= 4) {
                    /*
                    // request processing
                    Message requestData = new MessageImpl();
                    requestData.setIntValue(tmp.readInt());
                    Message responseData = new MessageImpl();
                    responseData.setIntValue(requestData.getIntValue() * 2);
                    ChannelFuture future = ctx.writeAndFlush(responseData);
                    future.addListener(ChannelFutureListener.CLOSE);
                    */
                }
            }
        }
    }

    //NETTY classes

}
