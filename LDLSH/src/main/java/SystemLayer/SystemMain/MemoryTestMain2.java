package SystemLayer.SystemMain;

import Factories.ComponentFactories.MainFactory;
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
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryTestMain2 extends SystemMainImp {


    public MemoryTestMain2(String[] args, DataContainer appContext) throws Exception {
        super(args, appContext);
    }

    @Override
    public void run() throws Exception {
        //Commands
        // cmd <file> <lines>
        String fileName = args[1];
        Integer lines = Integer.parseInt(args[2]);

        //LoadFile
        List<DataObject<String>> data = new ArrayList<>();
        BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName));
        int i = 0;
        while ( i<lines ) {
            String line = fileBufferReader.readLine();
            if (!line.isEmpty() || !line.isBlank()) {
                DataObject<String> dataObject = (DataObject<String>) system.newDataObject();
                dataObject.setValues(line);
                data.add(dataObject);
                i++;
                System.out.println("Loaded: " + i);
            }
        }

        //Execute
        AtomicInteger successCounter = new AtomicInteger();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Done. Please Check memory.");
        scanner.nextLine();

        //Insert File
        System.out.println("Inserting...");
        for (DataObject<String> dataElement : data) {
            //System.out.println("adding:"+ dataElement.getValues());
            //Execute instruction
            Timestamp initialTimeStamp = new Timestamp(System.currentTimeMillis());
            ListenableFuture<DataObject<?>> result = system.insert(dataElement);
            Futures.addCallback(result, new FutureCallback<>() {
                final DataObject<?> elem = dataElement;
                final Timestamp initialTimeStamp1 = initialTimeStamp;

                @Override
                public void onSuccess(DataObject object) {
                    //Complete
                    Timestamp finalTimestamp = new Timestamp(System.currentTimeMillis());
                    int i ;
                    synchronized (successCounter) {
                        i = successCounter.getAndIncrement();
                    }
                    long totalExecutionTime = finalTimestamp.getTime() - initialTimeStamp1.getTime();
                    System.out.println(i + "insert for " + elem.getValues() + " execution time: " + totalExecutionTime + " ms");
                }

                @Override
                public void onFailure(@NotNull Throwable throwable) {
                    System.err.println("Insert Failed: " + throwable.getMessage());
                    throwable.printStackTrace();
                }
            }, appContext.getCallbackExecutor());
        }
        System.out.println("Done. Please Check memory.");
        scanner.nextLine();

        successCounter.set(0);

        System.out.println("Querying...");
        for (DataObject<?> dataElement : data){

            //Execute Instruction
            Timestamp initialTimeStamp = new Timestamp(System.currentTimeMillis());
            ListenableFuture<DataObject<?>> result = system.query(dataElement);
            Futures.addCallback(result, new FutureCallback<>() {
                final DataObject<?> elem = dataElement;
                final Timestamp initialTimeStamp1 = initialTimeStamp;
                @Override
                public void onSuccess(DataObject object) {
                    //Complete
                    Timestamp finalTimestamp = new Timestamp(System.currentTimeMillis());
                    int n;
                    synchronized (successCounter) {
                        n = successCounter.getAndIncrement();
                    }
                    long totalExecutionTime = finalTimestamp.getTime() - initialTimeStamp1.getTime();
                    System.out.println(n + " - query for "+ elem.getValues() +" execution time: "+ totalExecutionTime+" ms");
                }

                @Override
                public void onFailure(@NotNull Throwable throwable) {
                    //Completed with error
                }
            }, appContext.getCallbackExecutor());
        }
        System.out.println("Done. Please Check memory.");
        scanner.nextLine();

        //Shutdown
        system.stop();
        System.exit(0);

    }
}
