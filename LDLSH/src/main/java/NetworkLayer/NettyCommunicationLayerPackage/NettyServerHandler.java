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
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private DataContainer appContext;
    private ExecutorService callBackExecutor;

    public NettyServerHandler( DataContainer appContext ){
        setAppContext( appContext );
    }

    public void setAppContext( DataContainer appContext ){
        this.appContext = appContext;
        this.callBackExecutor = Executors.newFixedThreadPool(5);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Handler added from " + ctx.channel().remoteAddress() + " to: " + ctx.channel().localAddress());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Handler removed from "  + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message message = (Message) msg;

        System.out.println( "Received message from " + ctx.channel().remoteAddress() + "->" + message.getType() );
        //Process
        try {
            switch (message.getType()) {
                //Multimap Server Side
                case COMPLETION_MESSAGE -> {
                    MultimapTask task = new CompletionMultimapTask(message, appContext);
                    ListenableFuture<Message> responseFuture = appContext.getExecutorService().submit(task);

                    FutureCallback<Message> responseCallback = new FutureCallback<Message>() {
                        @Override
                        public void onSuccess(Message response) {
                            response.setTransactionId(message.getTransactionId());
                            ctx.writeAndFlush(response);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //Create message body
                            List<Object> responseBody = new ArrayList<>();
                            responseBody.add("ERROR:Performing operation");
                            responseBody.add(throwable.getMessage() );
                            Message response = new MessageImpl( Message.types.COMPLETION_RESPONSE, responseBody );//Create response
                            response.setTransactionId(message.getTransactionId() ); //Assign transaction id
                            ctx.writeAndFlush(response); //Send response
                        }
                    };
                    Futures.addCallback( responseFuture, responseCallback, callBackExecutor );
                }

                case INSERT_MESSAGE -> {
                    if( message.getBody().size() != 3 )
                        throw new Exception("Invalid body Size for message type: INSERT_MESSAGE");

                    MultimapTask task = new InsertMultimapTask(message, appContext);
                    ListenableFuture<Message> responseFuture = appContext.getExecutorService().submit(task);
                    FutureCallback<Message> responseCallback = new FutureCallback<Message>() {
                        @Override
                        public void onSuccess(Message response) {
                            response.setTransactionId(message.getTransactionId());
                            ctx.writeAndFlush(response);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //Create message body
                            List<Object> responseBody = new ArrayList<>();
                            responseBody.add("ERROR:Performing operation");
                            responseBody.add(throwable.getMessage() );
                            Message response = new MessageImpl( Message.types.INSERT_MESSAGE_RESPONSE, responseBody );//Create response
                            response.setTransactionId(message.getTransactionId() ); //Assign transaction id
                            ctx.writeAndFlush(response); //Send response
                        }
                    };
                    Futures.addCallback( responseFuture, responseCallback, callBackExecutor );
                }

                //Queries a message through the multi maps.
                case QUERY_MESSAGE_SINGLE_BLOCK -> {
                    //Query
                    QueryMultimapTask task = new QueryMultimapTask(message, appContext);
                    ListenableFuture<Message> responseFuture = appContext.getExecutorService().submit(task);
                    FutureCallback<Message> responseCallback = new FutureCallback<Message>() {
                        @Override
                        public void onSuccess(Message response) {
                            response.setTransactionId(message.getTransactionId());
                            ctx.writeAndFlush(response);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //Create message body
                            List<Object> responseBody = new ArrayList<>();
                            responseBody.add("ERROR:Performing operation");
                            responseBody.add(throwable.getMessage() );
                            Message response = new MessageImpl( Message.types.QUERY_RESPONSE, responseBody );//Create response
                            response.setTransactionId(message.getTransactionId() ); //Assign transaction id
                            ctx.writeAndFlush(response); //Send response
                        }
                    };
                    Futures.addCallback( responseFuture, responseCallback, callBackExecutor );
                }
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
}
