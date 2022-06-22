package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyProcessingHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf tmp;
    private DataContainer appContext;

    public void setAppContext( DataContainer appContext ){
        this.appContext = appContext;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Handler added");
        tmp = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Handler removed");
        tmp.release();
        tmp = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        tmp.writeBytes(m);
        m.release();
        if (tmp.readableBytes() >= 4) {
            Message message = (Message) msg;
            //Process
            appContext.
        }
    }
}
