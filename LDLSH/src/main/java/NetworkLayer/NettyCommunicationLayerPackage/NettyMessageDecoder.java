package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class NettyMessageDecoder extends ReplayingDecoder<Message> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Message.types type = Message.types.values()[in.readByte()]; //Decode Message type
        byte[] body = new byte[in.readableBytes()-1];
        in.readBytes(body);

        ByteArrayInputStream bis = new ByteArrayInputStream(body);
        ObjectInputStream ois = new ObjectInputStream( bis );

        while (ois.available()>0){
            Object object = ois.readObject();
            out.add(object);
        }
    }
}
