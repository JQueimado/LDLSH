package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import NetworkLayer.MessageImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class NettyMessageDecoder extends ReplayingDecoder<Message> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(in.writerIndex());
        byte[] body = new byte[in.writerIndex()];
        in.readBytes(body);

        ByteArrayInputStream bis = new ByteArrayInputStream(body);
        ObjectInputStream ois = new ObjectInputStream( bis );
        out.add( ois.readObject() );
    }
}
