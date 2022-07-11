import Factories.ComponentFactories.SystemServerFactory;
import SystemLayer.Components.SystemServer.SystemImpl;
import SystemLayer.Components.SystemServer.SystemServer;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

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
                                system.insert(temp);
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
