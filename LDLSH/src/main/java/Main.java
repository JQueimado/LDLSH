import SystemLayer.Components.SystemServer.SystemImpl;
import SystemLayer.Components.SystemServer.SystemServer;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final String ui_config = "UI";

    public static void main(String[] args) throws Exception {
        //Create context
        DataContainer dataContainer = new DataContainer(args[0]);

        //Crete application
        SystemServer system = new SystemImpl(dataContainer);

        //UI configurations
        boolean ui;

        try{
            ui = Boolean.parseBoolean( dataContainer.getConfigurator().getConfig(ui_config) );
        }catch (Exception e){
            ui = false;
        }

        if( ui ) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("" +
                    "InsertInstruction:\n" +
                    "I <string> -> Inserts the <string> object into the database\n" +
                    "Q <string> -> Queries the <string> and returns its Nearest Neighbor\n" +
                    "IF <string> -> Inserts all values from the file <string> to the database line by line\n" +
                    "QF <string> -> Queries all values from the file <string>\n" +
                    "E -> exit\n");
            boolean exit = false;
            while (!exit) {

                String op = scanner.nextLine();

                String[] ops = op.split(" ");

                switch (ops[0]) {
                    //Insert
                    case "I" -> {
                        DataObject<String> dataObject = (DataObject<String>) system.newDataObject();
                        dataObject.setValues(ops[1]);
                        Future<DataObject<?>> future = system.insert(dataObject);
                        future.get();
                        System.out.println("done");
                    }

                    //Query
                    case "Q" -> {
                        DataObject<String> dataObject = (DataObject<String>) system.newDataObject();
                        dataObject.setValues(ops[1]);
                        Future<DataObject<?>> resultFuture = system.query(dataObject);

                        DataObject<String> result = (DataObject<String>) resultFuture.get();

                        if (result == null) {
                            System.out.println("System returned null value");
                            continue;
                        }

                        System.out.printf("Result:%s\n", result.getValues());
                    }

                    //Insert File
                    case "IF" -> {
                        try {
                            String fileName = ops[1];
                            //if( ops.length == 3 )

                            BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName));
                            String line;

                            AtomicInteger counter1 = new AtomicInteger();
                            AtomicInteger counter2 = new AtomicInteger();
                            while ((line = fileBufferReader.readLine()) != null ){
                                if(line.isEmpty() || line.isBlank())
                                    continue;
                                counter1.getAndIncrement();
                                System.out.println("Inserting object: "+ line);
                                DataObject<String> temp = (DataObject<String>) system.newDataObject();
                                temp.setValues(line);
                                ListenableFuture<DataObject<?>> result = system.insert(temp);
                                Futures.addCallback(result, new FutureCallback<>() {
                                    final DataObject<String> currentObject = temp;
                                    @Override
                                    public void onSuccess(DataObject<?> object) {
                                        if( object == null )
                                            System.err.println("Insert failed for: " + currentObject.getValues());
                                        else {
                                            System.out.println("Insert complete for object: " + object.getValues());
                                            synchronized (counter2) {
                                                counter2.getAndIncrement();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Throwable throwable) {
                                        System.err.println("Insert Failed: " + throwable.getMessage());
                                        throwable.printStackTrace();
                                    }
                                }, dataContainer.getCallbackExecutor());
                            }
                            //Wait for operations
                            if(dataContainer.getDebug())
                                System.out.println("Main: Waiting executor service to stop");
                            dataContainer.getExecutorService().awaitTermination(10, TimeUnit.SECONDS);
                            if(dataContainer.getDebug())
                                System.out.println("Main: Waiting callback executor to stop");
                            dataContainer.getCallbackExecutor().awaitTermination(10, TimeUnit.SECONDS);
                            //Eval
                            if( counter1.get() != counter2.get() ) {
                                int dif = counter1.get() - counter2.get();
                                throw new Exception("ERROR: " + dif + " operations were not performed");
                            }
                            System.out.println("Done");
                        }catch (IOException e){
                            System.err.println("File not found");
                            e.printStackTrace();
                        }
                    }

                    case "QF" -> {
                        try {
                            String fileName = ops[1];
                            //if( ops.length == 3 )

                            BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName));
                            String line;

                            AtomicInteger counter1 = new AtomicInteger();
                            AtomicInteger counter2 = new AtomicInteger();
                            while ((line = fileBufferReader.readLine()) != null ){
                                if(line.isEmpty() || line.isBlank())
                                    continue;
                                counter1.getAndIncrement();
                                System.out.println("Querying object: "+ line);
                                DataObject<Object> temp = (DataObject<Object>) system.newDataObject();
                                temp.setValues(line);
                                ListenableFuture<DataObject<?>> result = system.query(temp);

                                Futures.addCallback(result, new FutureCallback<>() {
                                    @Override
                                    public void onSuccess(DataObject<?> object) {
                                        DataObject<String> stringDataObject = (DataObject<String>) object;
                                        if( object == null )
                                            System.out.println("-------\n" +
                                                    "Query for object:\n" +
                                                    temp.getValues() +
                                                    "\nCompleted with a null value.");
                                        else
                                            System.out.println("-------\n" +
                                                    "Query for object:\n" +
                                                    temp.getValues() +
                                                    "\nCompleted with result:\n"
                                                    + object.getValues());
                                        counter2.getAndIncrement();
                                    }

                                    @Override
                                    public void onFailure(@NotNull Throwable throwable) {
                                        System.err.println("Query Failed: " + throwable.getMessage());
                                        throwable.printStackTrace();
                                    }
                                }, dataContainer.getCallbackExecutor());
                            }
                            //Wait for operations
                            if(dataContainer.getDebug())
                                System.out.println("Main: Waiting executor service to stop");
                            dataContainer.getExecutorService().awaitTermination(10, TimeUnit.SECONDS);
                            if(dataContainer.getDebug())
                                System.out.println("Main: Waiting callback executor to stop");
                            dataContainer.getCallbackExecutor().awaitTermination(10, TimeUnit.SECONDS);
                            //Eval
                            if( counter1.get() != counter2.get() ) {
                                int dif = counter1.get() - counter2.get();
                                throw new Exception("ERROR: " + dif + " operations were not performed");
                            }
                            System.out.println("Done");
                        }catch (IOException e){
                            System.err.println("File not found");
                            e.printStackTrace();
                        }
                    }

                    //Exit
                    case "E" -> {
                        exit = true;
                        system.stop();
                    }

                    //Unknown Op
                    default -> {
                        System.out.println("Invalid OP");
                    }
                }
            }
        }
    }
}
