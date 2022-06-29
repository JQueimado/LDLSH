package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import NetworkLayer.MessageImpl;
import SystemLayer.Components.MultiMapImpl.MultiMap.MultiMapValue;
import SystemLayer.Components.TaskImpl.Multimap.CompletionMultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.InsertMultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.MultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.QueryMultimapTask;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHashImpl;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import com.google.common.util.concurrent.ListenableFuture;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
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
            try {
                switch (message.getType()) {
                    case COMPLETION_MESSAGE -> {
                        if( message.getBody().size() != 2 )
                            throw new Exception("Invalid body Size for message type: COMPLETION_MESSAGE");

                        LSHHash hash = (LSHHash) message.getBody().get(0);
                        UniqueIdentifier uid = (UniqueIdentifier) message.getBody().get(1);

                        MultimapTask task = new CompletionMultimapTask(hash, uid, appContext);
                        appContext.getExecutorService().submit(task);
                    }

                    case COMPLETION_RESPONSE -> {

                    }

                    case INSERT_MESSAGE -> {
                        if( message.getBody().size() != 3 )
                            throw new Exception("Invalid body Size for message type: INSERT_MESSAGE");

                        MultimapTask<Boolean> task = new InsertMultimapTask(message, appContext);
                        ListenableFuture<Boolean> result = appContext.getExecutorService().submit(task);
                        result.addListener(() ->{
                            Boolean bool;
                            try {
                                bool = result.get();
                            }catch (Exception e){
                                bool = false;
                            }
                            List<Serializable> responseBody = new ArrayList<>();
                            responseBody.add(bool);
                            Message response = new MessageImpl(Message.types.QUERY_RESPONSE, responseBody);
                            ctx.writeAndFlush(response);
                        }, appContext.getExecutorService());
                    }

                    case QUERY_MESSAGE_SINGLE_BLOCK -> {
                        if( message.getBody().size() != 1 ){
                            throw new Exception("Invalid body Size for message type: QUERY_MESSAGE_SINGLE_BLOCK");
                        }

                        LSHHashImpl.LSHHashBlock block = (LSHHashImpl.LSHHashBlock) message.getBody().get(0);

                        QueryMultimapTask task = new QueryMultimapTask(block, appContext);
                        ListenableFuture<List<MultiMapValue>> result = appContext.getExecutorService().submit(task);
                        result.addListener( () ->{
                            List<Serializable> responseBody;
                            try {
                                responseBody = result.get();
                            }catch ( Exception e ){
                                responseBody = new ArrayList<>();
                                responseBody.add("Error:"+e.getMessage());
                            }
                            Message queryResult = new MessageImpl(Message.types.QUERY_RESPONSE, responseBody);
                            ctx.writeAndFlush(queryResult);
                        } );
                    }
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }
}
