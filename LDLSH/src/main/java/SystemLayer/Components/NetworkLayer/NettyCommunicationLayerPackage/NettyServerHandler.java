package SystemLayer.Components.NetworkLayer.NettyCommunicationLayerPackage;

import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.NetworkLayer.MessageImpl;
import SystemLayer.Components.TaskImpl.Multimap.MultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.QueryMultimapTask;
import SystemLayer.Containers.DataContainer;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

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
        //if( appContext.getDebug() )
        //    System.out.println("NettyServerHandler: Handler added from " + ctx.channel().remoteAddress() + " to: " + ctx.channel().localAddress());
        temp = ctx.alloc().directBuffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        //if( appContext.getDebug() )
        //    System.out.println("NettyServerHandler: Handler removed from "  + ctx.channel().remoteAddress());
        if( temp.release() )
            temp = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //if( appContext.getDebug() )
        //    System.out.println("NettyServerHandler: Received " + ((ByteBuf) msg).readableBytes() + "bytes");
        temp.writeBytes( (ByteBuf) msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //if( appContext.getDebug() )
        //    System.out.println("NettyServerHandler: Read Complete. Buffer Size: "+ temp.readableBytes());
        while (temp.readableBytes() > 0) {
            //Decode
            byte[] body = new byte[temp.readableBytes()];
            temp.readBytes(body);

            //if (appContext.getDebug())
            //    System.out.println("NettyServerHandler: Start Buffer");

            ByteArrayInputStream bis = new ByteArrayInputStream(body);

            Message message = null;
            try {
                //if (appContext.getDebug())
                //    System.out.println("NettyServerHandler: Read Buffer");
                ObjectInputStream ois = new ObjectInputStream(bis);
                message = (Message) ois.readObject();

            } catch (Exception e) {
                //if (appContext.getDebug())
                //    System.out.println("NettyServerHandler: Read Failed resetting message buffer");
                temp.writeBytes(body);
                return;
            }
            /*catch (StreamCorruptedException e) {
                System.out.println("NettyServerHandler: Found Corrupt message");
                e.printStackTrace();
                //temp.clear();
                //temp.writeBytes(bis.readAllBytes());
            }
             */

            temp.writeBytes( bis.readAllBytes() );

            //if (appContext.getDebug())
            //    System.out.println("NettyServerHandler: Remaining Bytes to process: " + temp.readableBytes());

            if(message == null)
                continue;
            //Process
            //if (appContext.getDebug())
            //    System.out.println("NettyServerHandler: Received " + message.getType()
            //            + " message from " + ctx.channel().remoteAddress()
            //    );

            //Process
            try {
                final int messageID = message.getTransactionId();

                switch (message.getType()) {
                    //Multimap Server Side
                    case COMPLETION_MESSAGE -> {
                        MultimapTask task = appContext.getMultimapTaskFactory().getNewCompletionQueryTask(message);
                        ListenableFuture<Message> responseFuture = appContext.getExecutorService().submit(task);

                        FutureCallback<Message> responseCallback = new FutureCallback<>() {
                            @Override
                            public void onSuccess(Message response) {
                                response.setTransactionId(messageID);
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
                                responseBody.add(throwable.getMessage());
                                Message response = new MessageImpl(Message.types.COMPLETION_RESPONSE, responseBody);//Create response
                                response.setTransactionId(messageID); //Assign transaction id
                                synchronized (bufferWriteLock) {
                                    ctx.write(response); //Send
                                    ctx.flush();
                                }
                            }
                        };
                        Futures.addCallback(responseFuture, responseCallback, callBackExecutor);
                    }

                    case INSERT_MESSAGE -> {
                        if (message.getBody().size() != 2)
                            throw new Exception("Invalid body Size for message type: INSERT_MESSAGE");

                        MultimapTask task = appContext.getMultimapTaskFactory().getNewMultimapInsertTask(message);
                        ListenableFuture<Message> responseFuture = appContext.getExecutorService().submit(task);
                        FutureCallback<Message> responseCallback = new FutureCallback<>() {
                            @Override
                            public void onSuccess(Message response) {
                                response.setTransactionId(messageID);
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
                                responseBody.add(throwable.getMessage());
                                Message response = new MessageImpl(Message.types.INSERT_MESSAGE_RESPONSE, responseBody);//Create response
                                response.setTransactionId(messageID); //Assign transaction id
                                synchronized (bufferWriteLock) {
                                    ctx.write(response); //Send
                                    ctx.flush();
                                }
                            }
                        };
                        Futures.addCallback(responseFuture, responseCallback, callBackExecutor);
                    }

                    //Queries a message through the multi maps.
                    case QUERY_MESSAGE_SINGLE_BLOCK, QUERY_MESSAGE -> {
                        //Query
                        MultimapTask task = appContext.getMultimapTaskFactory().getNewMultimapQueryTask(message);
                        ListenableFuture<Message> responseFuture = appContext.getExecutorService().submit(task);
                        FutureCallback<Message> responseCallback = new FutureCallback<>() {
                            @Override
                            public void onSuccess(Message response) {
                                response.setTransactionId(messageID);
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
                                responseBody.add(throwable.getMessage());
                                Message response = new MessageImpl(Message.types.QUERY_RESPONSE, responseBody);//Create response
                                response.setTransactionId(messageID); //Assign transaction id
                                synchronized (bufferWriteLock) {
                                    ctx.write(response);
                                    ctx.flush();
                                }
                            }
                        };
                        Futures.addCallback(responseFuture, responseCallback, callBackExecutor);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
