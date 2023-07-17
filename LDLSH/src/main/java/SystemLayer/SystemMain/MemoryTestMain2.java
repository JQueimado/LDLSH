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
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryTestMain2 extends SystemMainImp {


    public MemoryTestMain2(String[] args, DataContainer appContext) throws Exception {
        super(args, appContext);
    }

    int TIMEOUT=10000;

    @Override
    public void run() throws Exception {
        //Commands
        // cmd <file> <lines>
        String op = args[1];
        String fileName = args[2];

        //LoadFile
        List<DataObject<String>> data = new ArrayList<>();
        BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName));

        System.out.println("Loading data-set");

        String line;
        while((line = fileBufferReader.readLine()) != null){
            if (!line.isEmpty() || !line.isBlank()) {
                DataObject<String> dataObject = (DataObject<String>) system.newDataObject();
                dataObject.setValues(line);
                data.add(dataObject);
            }
        }

        AtomicInteger successCounter = new AtomicInteger();

        //Execute
//        System.out.println("Done. Please Check memory.");
//        /*
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine();
//        */
//        Thread.sleep(TIMEOUT);

        //Insert File
        switch (op) {
            case "-i" -> {
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
                            int i;
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
            }

            case "-q" -> {
                System.out.println("Querying...");
                for (DataObject<?> dataElement : data) {

                    //Execute Instruction
                    Timestamp initialTimeStamp = new Timestamp(System.currentTimeMillis());
                    ListenableFuture<DataObject<?>> result = system.query(dataElement);
                    Futures.addCallback(result, new FutureCallback<>() {
                        final DataObject<?> elem = dataElement;
                        final Timestamp initialTimeStamp1 = initialTimeStamp;
                        @Override
                        public void onSuccess(DataObject<?> object) {
                            //Complete
                            Timestamp finalTimestamp = new Timestamp(System.currentTimeMillis());
                            int n;
                            synchronized (successCounter) {
                                n = successCounter.getAndIncrement();
                            }
                            long totalExecutionTime = finalTimestamp.getTime() - initialTimeStamp1.getTime();

                            if ( object == null ) {
                                System.out.println(n + " - query for " + elem.getValues() +
                                        " returned: null" +
                                        " execution time: " + totalExecutionTime + " ms");
                            }else {
                                System.out.println(n + " - query for " + elem.getValues() +
                                        " returned: " + object.getValues() +
                                        " execution time: " + totalExecutionTime + " ms");
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Throwable throwable) {
                            //Completed with error
                        }
                    }, appContext.getCallbackExecutor());
                }
            }
        }
        //Shutdown
        system.stop();
        if (successCounter.get() != data.size() )
            throw new Exception("Not all Inserts were performed. Missing: " + (data.size() - successCounter.get()) );

        System.out.println("Done. Please Check memory.");
        //scanner.nextLine();
        Thread.sleep(TIMEOUT);

        System.exit(0);

    }
}
