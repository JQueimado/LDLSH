package NetworkLayer.NettyCommunicationLayerPackage;

import NetworkLayer.Message;
import NetworkLayer.MessageImpl;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Components.TaskImpl.Multimap.CompletionMultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.InsertMultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.MultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.QueryMultimapTask;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHashImpl;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {
    private DataContainer appContext;
    private ExecutorService callBackExecutor;

    public void setAppContext( DataContainer appContext ){
        this.appContext = appContext;
        this.callBackExecutor = Executors.newFixedThreadPool(5);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Handler added from" + ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Handler removed from "  + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message message) {
        System.out.println( "Received message from " + ctx.channel().remoteAddress() + "->" + message.getType() );

        //Process
        try {
            switch (message.getType()) {
                //Multimap Server Side
                case COMPLETION_MESSAGE -> {
                    MultimapTask task = new CompletionMultimapTask(message, appContext);
                    ListenableFuture<Message> responseFuture = appContext.getExecutorService().submit(task);
                    responseFuture.addListener( ()->{
                        try {
                            Message response = responseFuture.get();
                            ctx.writeAndFlush( response );
                        }catch (Exception e){
                            e.printStackTrace();
                            ctx.writeAndFlush("ERROR:Performing operation");
                        }
                    }, callBackExecutor);
                }

                case INSERT_MESSAGE -> {
                    if( message.getBody().size() != 3 )
                        throw new Exception("Invalid body Size for message type: INSERT_MESSAGE");

                    MultimapTask task = new InsertMultimapTask(message, appContext);
                    ListenableFuture<Message> result = appContext.getExecutorService().submit(task);
                    result.addListener(() ->{
                        try {
                            Message response = result.get();
                            ctx.writeAndFlush( response );
                        }catch (Exception e){
                            e.printStackTrace();
                            ctx.writeAndFlush("ERROR:Performing operation");
                        }
                    }, callBackExecutor);
                }

                //Queries a message through the multi maps.
                case QUERY_MESSAGE_SINGLE_BLOCK -> {
                    //Query
                    QueryMultimapTask task = new QueryMultimapTask(message, appContext);
                    ListenableFuture<Message> result = appContext.getExecutorService().submit(task);
                    //Add listener to resulting future
                    result.addListener( () ->{
                        try {
                            Message response = result.get();
                            ctx.writeAndFlush(response);
                        }catch (Exception e){
                            e.printStackTrace();
                            ctx.writeAndFlush("ERROR:Performing operation");
                        }
                    }, callBackExecutor );
                }
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
}
