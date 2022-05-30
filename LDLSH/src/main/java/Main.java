import Factories.ComponentFactories.SystemServerFactory;
import SystemLayer.Components.SystemServer.SystemServer;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.Scanner;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws Exception {
        //Create context
        DataContainer dataContainer = new DataContainer(args[0]);

        //Crete application
        SystemServerFactory systemServerFactory = new SystemServerFactory();
        SystemServer system = systemServerFactory.newSystemServer(dataContainer);

        //UI configurations
        Scanner scanner = new Scanner(System.in);

        System.out.print("" +
                "InsertInstruction:\n" +
                "I <string> -> Inserts the <string> object into the database\n" +
                "Q <string> -> Queries the <string> and returns its Nearest Neighbor\n" +
                "E -> exit\n");
        boolean exit = false;
        while (!exit){

            String op = scanner.nextLine();

            String[] ops = op.split(" ");
            if( ops.length > 2 ){
                System.out.println("Invalid Input");
                continue;
            }

            //Create data object
            DataObject dataObject = dataContainer.getDataObjectFactory().getNewDataObject(
                    dataContainer.getConfigurator().getConfig("DATA_OBJECT")
            );

            dataObject.setValues(ops[1]);

            switch (ops[0]){
                //Insert
                case "I" -> {
                    Future future = system.insert(dataObject);
                    future.get();
                    System.out.println("done");
                }

                //Query
                case "Q" -> {
                    DataObject result = system.query(dataObject);

                    if ( result == null ){
                        System.out.println("System returned null value");
                        continue;
                    }

                    DataObject<String> string = (DataObject<String>) result;

                    System.out.printf("Result:%s\n", string.getValues() );
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
