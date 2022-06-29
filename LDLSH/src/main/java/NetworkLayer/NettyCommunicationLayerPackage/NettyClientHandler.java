package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf tmp;
    private DataContainer appContext;

    public NettyClientHandler(  ){

    }

    public void setAppContext( DataContainer appContext ){
        this.appContext = appContext;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        tmp = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        tmp.release();
        tmp = null;
    }

    //Server Responses
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        tmp.writeBytes(m);
        m.release();
        if (tmp.readableBytes() >= 4) {
            Message message = (Message) msg;
            //Process
            try {
                switch (message.getType()) {

                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }
}
