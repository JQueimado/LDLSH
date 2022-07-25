package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import SystemLayer.Containers.DataContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.*;

public class NettyMessageEncoder extends MessageToByteEncoder<Message> {

    private final DataContainer appContext;

    public NettyMessageEncoder(DataContainer appContext){
        this.appContext = appContext;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf)
            throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( bos );
        oos.writeObject( message );
        oos.flush();
        byteBuf.writeBytes(bos.toByteArray());
        //if( appContext.getDebug() )
        //    System.out.println( "Sent "+message.getType()+" message of size: " + byteBuf.readableBytes() );
    }
}
