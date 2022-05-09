import Factories.ComponentFactories.SystemServerFactory;
import Factories.DataFactories.DataObjectFactory;
import SystemLayer.Components.SystemServer.CentralizedSystem;
import SystemLayer.Components.SystemServer.SystemServer;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        //Create context
        DataContainer dataContainer = new DataContainer(args[0]);

        //Crete application
        SystemServerFactory systemServerFactory = new SystemServerFactory();
        SystemServer system = systemServerFactory.newSystemServer(dataContainer);

        //UI configurations
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.print("" +
                    "InsertInstruction:\n" +
                    "I <string> -> Inserts the <string> object into the database\n" +
                    "Q <string> -> Queries the <string> and returns its Nearest Neighbor\n");

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
                case "I" -> {
                    system.insert(dataObject);
                }

                case "Q" -> {
                    DataObject result = system.query(dataObject);

                    if ( result == null ){
                        System.out.println("System returned null value");
                        continue;
                    }

                    DataObject<String> string = (DataObject<String>) result;

                    System.out.printf("Result:%s\n", string.getValues() );
                }

                default -> {
                    System.out.println("Invalid OP");
                }
            }
        }
    }
}
