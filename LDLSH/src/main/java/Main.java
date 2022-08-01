import SystemLayer.Components.SystemServer.SystemImpl;
import SystemLayer.Components.SystemServer.SystemServer;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Future;

public class Main {

    private static final String ui_config = "UI";

    public static void main(String[] args) throws Exception {
        //Create context
        DataContainer dataContainer = new DataContainer(args[0]);

        //Crete application
        SystemServer system = new SystemImpl(dataContainer);

        //Commands
        //insert: -i <file>
        //query: -q <file>
        String op = args[1];
        String fileName = args[2];

        //LoadFile
        List<DataObject<String>> data = new ArrayList<>();
        BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName));
        String line;
        while((line = fileBufferReader.readLine()) != null)
            if (!line.isEmpty() || !line.isBlank()) {
                DataObject dataObject = system.newDataObject();
                dataObject.setValues(line);
                data.add( dataObject );
            }

        //Execute
        switch (op) {
            //Insert File
            case "-i" -> {
                for (DataObject dataElement : data){
                    //System.out.println("adding:"+ dataElement.getValues());
                    //Execute instruction
                    ListenableFuture<DataObject> result = system.insert(dataElement);
                    Futures.addCallback(result, new FutureCallback<DataObject>() {
                        @Override
                        public void onSuccess(DataObject object) {
                            //Nothing
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            System.err.println("Insert Failed: " + throwable.getMessage());
                            throwable.printStackTrace();
                        }
                    }, dataContainer.getCallbackExecutor());
                }
                //System.out.println("done");
                dataContainer.getExecutorService().shutdown(); //waits all tasks termination
                //assert i[0] == operations;
                System.exit(0);
            }

            case "-q" -> {
                for (DataObject dataElement : data){

                    //Execute Instruction
                    ListenableFuture<DataObject> result = system.query(dataElement);

                    Futures.addCallback(result, new FutureCallback<DataObject>() {
                        final DataObject e = dataElement;
                        @Override
                        public void onSuccess(DataObject object) {
                            if( object != null )
                                System.out.println(e.getValues() + " -> " + object.getValues());
                            else
                                System.out.println(e.getValues() + " -> null" );
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //Completed with error
                        }
                    }, dataContainer.getCallbackExecutor());
                }
                dataContainer.getExecutorService().shutdown(); //waits all tasks termination
                //assert i[0] == operations;
                System.exit(0);
            }
        }
    }
}
