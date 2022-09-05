package SystemLayer.SystemMain;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TraditionalLocalThroughputTestMain extends SystemMainImp {

    public TraditionalLocalThroughputTestMain(String[] args, DataContainer appContext) throws Exception {
        super(args, appContext);
    }

    @Override
    public void run() throws Exception {
        //Commands
        //insert: -i <file>
        //query: -q <file>
        String insertFilename = args[1];
        String queryFileName = args[2];

        //Load insert file
        List<DataObject<String>> insertData = new ArrayList<>();
        BufferedReader fileBufferReader = new BufferedReader(new FileReader(insertFilename));
        String line;
        while((line = fileBufferReader.readLine()) != null)
            if (!line.isEmpty() || !line.isBlank()) {
                DataObject<String> dataObject = (DataObject<String>) system.newDataObject();
                dataObject.setValues(line);
                insertData.add( dataObject );
            }

        //Load query file
        List<DataObject<String>> queryData = new ArrayList<>();
        fileBufferReader = new BufferedReader(new FileReader(queryFileName));
        while((line = fileBufferReader.readLine()) != null)
            if (!line.isEmpty() || !line.isBlank()) {
                DataObject<String> dataObject = (DataObject<String>) system.newDataObject();
                dataObject.setValues(line);
                queryData.add( dataObject );
            }

        //Insert
        Timestamp initialTimeStamp1 = new Timestamp(System.currentTimeMillis());
        final AtomicInteger successCounter = new AtomicInteger();
        for (DataObject<String> dataElement : insertData){
            ListenableFuture<DataObject<?>> result = system.insert(dataElement);
            Futures.addCallback(result, new FutureCallback<>() {
                @Override
                public void onSuccess(DataObject object) {
                    //Nothing
                    synchronized (successCounter) {
                        successCounter.getAndIncrement();
                    }
                }

                @Override
                public void onFailure(@NotNull Throwable throwable) {
                    System.err.println("Insert Failed: " + throwable.getMessage());
                    throwable.printStackTrace();
                }
            }, appContext.getCallbackExecutor());
        }

        Timestamp finalTimestamp = new Timestamp(System.currentTimeMillis());
        long totalExecutionTime = finalTimestamp.getTime() - initialTimeStamp1.getTime();
        System.out.println("done:\n" +
                "total execution time: "+ totalExecutionTime+" ms\n" +
                "throughput: "+ insertData.size()/(totalExecutionTime/1000) +" ops/s");


        //Query
        Timestamp initialTimeStamp2 = new Timestamp(System.currentTimeMillis());
        final AtomicInteger successCounter2 = new AtomicInteger();
        for (DataObject<?> dataElement : queryData){
            ListenableFuture<DataObject<?>> result = system.query(dataElement);
            Futures.addCallback(result, new FutureCallback<>() {
                final DataObject<?> elem = dataElement;
                final Timestamp timestamp = initialTimeStamp2;
                @Override
                public void onSuccess(DataObject object) {
                    synchronized (successCounter2) {
                        successCounter2.getAndIncrement();
                    }
                }

                @Override
                public void onFailure(@NotNull Throwable throwable) {
                    //Completed with error
                }
            }, appContext.getCallbackExecutor());
        }
        system.stop();
        if(successCounter2.get() != (queryData.size() + insertData.size()))
            throw new Exception("Error: Not all operations have ben completed");

        Timestamp finalTimestamp2 = new Timestamp(System.currentTimeMillis());
        totalExecutionTime = finalTimestamp2.getTime() - initialTimeStamp2.getTime();
        System.out.println("done:\n" +
                "total execution time: "+ totalExecutionTime+" ms\n" +
                "throughput: "+ queryData.size()/(totalExecutionTime/1000) +" ops/s");


        system.stop();
        System.exit(0);
    }
}
