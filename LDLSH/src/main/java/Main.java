import Factories.ComponentFactories.SystemServerFactory;
import SystemLayer.Components.SystemServer.SystemImpl;
import SystemLayer.Components.SystemServer.SystemServer;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Future;

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
                        DataObject dataObject = system.newDataObject();
                        dataObject.setValues(ops[1]);
                        Future future = system.insert(dataObject);
                        future.get();
                        System.out.println("done");
                    }

                    //Query
                    case "Q" -> {
                        DataObject dataObject = system.newDataObject();
                        dataObject.setValues(ops[1]);
                        Future<DataObject> resultFuture = system.query(dataObject);

                        DataObject result = resultFuture.get();

                        if (result == null) {
                            System.out.println("System returned null value");
                            continue;
                        }

                        DataObject<String> string = (DataObject<String>) result;

                        System.out.printf("Result:%s\n", string.getValues());
                    }

                    //Insert File
                    case "IF" -> {
                        try {
                            String fileName = ops[1];
                            //if( ops.length == 3 )

                            BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName));
                            String line;

                            while ((line = fileBufferReader.readLine()) != null ){
                                System.out.println("Inserting object: "+ line);
                                DataObject<Object> temp = system.newDataObject();
                                temp.setValues(line);
                                ListenableFuture<DataObject> result = system.insert(temp);
                                Futures.addCallback(result, new FutureCallback<DataObject>() {
                                    @Override
                                    public void onSuccess(DataObject object) {
                                        System.out.println("Insert complete for object: "+ object.getValues());
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        System.err.println("Insert Failed: " + throwable.getMessage());
                                        throwable.printStackTrace();
                                    }
                                }, dataContainer.getCallbackExecutor());

                            }
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

                            while ((line = fileBufferReader.readLine()) != null ){
                                if(line.isEmpty() || line.isBlank())
                                    continue;
                                System.out.println("Querying object: "+ line);
                                DataObject<Object> temp = system.newDataObject();
                                temp.setValues(line);
                                ListenableFuture<DataObject> result = system.query(temp);

                                Futures.addCallback(result, new FutureCallback<DataObject>() {
                                    @Override
                                    public void onSuccess(DataObject object) {
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
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        System.err.println("Query Failed: " + throwable.getMessage());
                                        throwable.printStackTrace();
                                    }
                                }, dataContainer.getCallbackExecutor());
                            }
                        }catch (IOException e){
                            System.err.println("File not found");
                            e.printStackTrace();
                        }
                    }

                    //Exit
                    case "E" -> {
                        exit = true;
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
