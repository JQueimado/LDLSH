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

public class TraditionalLocalLatencyTestMain extends SystemMainImp {

    public TraditionalLocalLatencyTestMain(String[] args, DataContainer appContext) throws Exception {
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
        final AtomicInteger successCounter = new AtomicInteger();
        for (DataObject<String> dataElement : insertData){
            Timestamp initialTimeStamp1 = new Timestamp(System.currentTimeMillis());
            ListenableFuture<DataObject<?>> result = system.insert(dataElement);
            Futures.addCallback(result, new FutureCallback<>() {
                final DataObject<String> elem = dataElement;
                final Timestamp timestamp = initialTimeStamp1;
                @Override
                public void onSuccess(DataObject object) {
                    Timestamp finalTimestamp = new Timestamp(System.currentTimeMillis());
                    long totalExecutionTime = finalTimestamp.getTime() - timestamp.getTime();
                    System.out.println("insert for " + elem.getValues() + " execution time: " + totalExecutionTime + " ms");
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

        //Query
        for (DataObject<?> dataElement : queryData){
            Timestamp initialTimeStamp2 = new Timestamp(System.currentTimeMillis());
            ListenableFuture<DataObject<?>> result = system.query(dataElement);
            Futures.addCallback(result, new FutureCallback<>() {
                final DataObject<?> elem = dataElement;
                final Timestamp timestamp = initialTimeStamp2;
                @Override
                public void onSuccess(DataObject object) {
                    //Complete
                    Timestamp finalTimestamp = new Timestamp(System.currentTimeMillis());
                    long totalExecutionTime = finalTimestamp.getTime() - timestamp.getTime();
                    System.out.println("query for "+ elem.getValues() +" execution time: "+ totalExecutionTime+" ms");

                    synchronized (successCounter) {
                        successCounter.getAndIncrement();
                    }
                }

                @Override
                public void onFailure(@NotNull Throwable throwable) {
                    //Completed with error
                }
            }, appContext.getCallbackExecutor());
        }

        system.stop();
        if(successCounter.get() != (queryData.size() + insertData.size() ))
            throw new Exception("Error: Not all operations have ben completed");

        System.exit(0);
    }
}
