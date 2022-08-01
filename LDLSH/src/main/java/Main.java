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
import java.sql.Timestamp;
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
        int operations = 0;
        while((line = fileBufferReader.readLine()) != null)
            if (!line.isEmpty() || !line.isBlank()) {
                DataObject dataObject = system.newDataObject();
                dataObject.setValues(line);
                data.add( dataObject );
                operations ++;
            }

        //Execute
        switch (op) {
            //Insert File
            case "-i" -> {
                for ( int i = 0; i < data.size(); i++){
                    DataObject dataElement = data.get(i);
                    //System.out.println("adding:"+ dataElement.getValues());
                    //Execute instruction
                    Timestamp initialTimeStamp = new Timestamp(System.currentTimeMillis());
                    ListenableFuture<DataObject> result = system.insert(dataElement);
                    Futures.addCallback(result, new FutureCallback<>() {
                        final DataObject elem = dataElement;
                        final Timestamp initialTimeStamp1 = initialTimeStamp;
                        @Override
                        public void onSuccess(DataObject object) {
                            //Complete
                            Timestamp finalTimestamp = new Timestamp(System.currentTimeMillis());
                            long totalExecutionTime = finalTimestamp.getTime() - initialTimeStamp1.getTime();
                            System.out.println("insert for "+ elem.getValues() +" execution time: "+ totalExecutionTime+" ms");
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            System.err.println("Insert Failed: " + throwable.getMessage());
                            throwable.printStackTrace();
                        }
                    }, dataContainer.getCallbackExecutor());
                }
                dataContainer.getExecutorService().shutdown(); //waits all tasks termination
                System.exit(0);
            }

            case "-q" -> {
                Timestamp initialTimeStamp = new Timestamp(System.currentTimeMillis());
                //final int[] i = {0};
                for (DataObject dataElement : data){

                    //Execute Instruction
                    ListenableFuture<DataObject> result = system.query(dataElement);

                    Futures.addCallback(result, new FutureCallback<DataObject>() {
                        @Override
                        public void onSuccess(DataObject object) {
                            //synchronized (i) {
                            //    i[0]++;
                            //}
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            //Completed with error
                        }
                    }, dataContainer.getCallbackExecutor());
                }
                dataContainer.getExecutorService().shutdown(); //waits all tasks termination
                //assert i[0] == operations;
                Timestamp finalTimestamp = new Timestamp(System.currentTimeMillis());
                long totalExecutionTime = finalTimestamp.getTime() - initialTimeStamp.getTime();
                System.out.println("done:\n" +
                        "total execution time: "+ totalExecutionTime+" ms\n" +
                        "throughput: "+ totalExecutionTime/operations +" ms/op");
                System.exit(0);
            }
        }
    }
}
