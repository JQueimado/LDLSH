package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.*;

public class NettyMessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf)
            throws Exception {
        byteBuf.writeByte( message.getType().ordinal() ); //Message type

        //Body encode
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( bos );
        for (Serializable object : message.getBody()) {
            oos.writeObject( object );
            oos.flush();
        }
        byteBuf.writeBytes(bos.toByteArray());
    }
}
