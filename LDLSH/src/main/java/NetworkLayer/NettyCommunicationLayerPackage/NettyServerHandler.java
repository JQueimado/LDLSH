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

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private DataContainer appContext;
    private ExecutorService callBackExecutor;
    private ByteBuf temp;
    private final Object bufferWriteLock;

    public NettyServerHandler( DataContainer appContext ){
        setAppContext( appContext );
        bufferWriteLock = new Object();
    }

    public void setAppContext( DataContainer appContext ){
        this.appContext = appContext;
        this.callBackExecutor = appContext.getCallbackExecutor();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Handler added from " + ctx.channel().remoteAddress() + " to: " + ctx.channel().localAddress());
        temp = ctx.alloc().directBuffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Handler removed from "  + ctx.channel().remoteAddress());
        temp.release();
        temp = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        temp.writeBytes( (ByteBuf) msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //Decode
        byte[] body = new byte[temp.writerIndex()];
        temp.readBytes(body);

        ByteArrayInputStream bis = new ByteArrayInputStream(body);
        ObjectInputStream ois = new ObjectInputStream( bis );

        //Process
        Message message = (Message) ois.readObject();
        System.out.println( "Received "+message.getType()
                +" message from "+ctx.channel().remoteAddress()
                +" of size: "+temp.writerIndex()
        );
        temp.release();
        temp = ctx.alloc().directBuffer();

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
                            synchronized (bufferWriteLock) {
                                ctx.write(response);
                                ctx.flush();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //Create message body
                            List<Object> responseBody = new ArrayList<>();
                            responseBody.add("ERROR:Performing operation");
                            responseBody.add(throwable.getMessage() );
                            Message response = new MessageImpl( Message.types.COMPLETION_RESPONSE, responseBody );//Create response
                            response.setTransactionId(message.getTransactionId() ); //Assign transaction id
                            synchronized (bufferWriteLock) {
                                ctx.write(response); //Send
                                ctx.flush();
                            }
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
                            synchronized (bufferWriteLock) {
                                ctx.write(response); //Send
                                ctx.flush();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //Create message body
                            List<Object> responseBody = new ArrayList<>();
                            responseBody.add("ERROR:Performing operation");
                            responseBody.add(throwable.getMessage() );
                            Message response = new MessageImpl( Message.types.INSERT_MESSAGE_RESPONSE, responseBody );//Create response
                            response.setTransactionId(message.getTransactionId() ); //Assign transaction id
                            synchronized (bufferWriteLock) {
                                ctx.write(response); //Send
                                ctx.flush();
                            }
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
                            synchronized (bufferWriteLock) {
                                ctx.write(response);
                                ctx.flush();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //Create message body
                            List<Object> responseBody = new ArrayList<>();
                            responseBody.add("ERROR:Performing operation");
                            responseBody.add(throwable.getMessage() );
                            Message response = new MessageImpl( Message.types.QUERY_RESPONSE, responseBody );//Create response
                            response.setTransactionId(message.getTransactionId() ); //Assign transaction id
                            synchronized (bufferWriteLock) {
                                ctx.write(response);
                                ctx.flush();
                            }
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
