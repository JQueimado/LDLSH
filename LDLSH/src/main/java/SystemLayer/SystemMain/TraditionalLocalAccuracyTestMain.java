package SystemLayer.SystemMain;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TraditionalLocalAccuracyTestMain extends SystemMainImp {

    public TraditionalLocalAccuracyTestMain(String[] args, DataContainer appContext) throws Exception {
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


        final AtomicInteger successCounter = new AtomicInteger();

        //InsertData
        for (DataObject<String> dataElement : insertData){
            //System.out.println("adding:"+ dataElement.getValues());
            //Execute instruction
            ListenableFuture<DataObject<?>> result = system.insert(dataElement);
            Futures.addCallback(result, new FutureCallback<>() {

                @Override
                public void onSuccess(DataObject object) {
                    System.out.println("insert: " + object.getValues());
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

        system.suspend();

        if(successCounter.get() != insertData.size())
            throw new Exception("Error: Not all operations have ben completed");

        final AtomicInteger successCounter2 = new AtomicInteger();

        for (DataObject<?> dataElement : queryData){

            //Execute Instruction
            ListenableFuture<DataObject<?>> result = system.query(dataElement);

            Futures.addCallback(result, new FutureCallback<>() {
                final DataObject<?> e = dataElement;
                @Override
                public void onSuccess(DataObject object) {
                    if( object != null )
                        System.out.println(e.getValues() + " -> " + object.getValues());
                    else
                        System.out.println(e.getValues() + " -> null" );
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

        system.suspend();

        if(successCounter2.get() != queryData.size())
            throw new Exception("Error: Not all operations have ben completed");

        system.stop();
        System.exit(0);
    }
}
