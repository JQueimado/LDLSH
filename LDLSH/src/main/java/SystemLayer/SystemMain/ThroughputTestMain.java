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
import java.util.concurrent.atomic.AtomicInteger;

public class ThroughputTestMain extends SystemMainImp {

    public ThroughputTestMain(String[] args, DataContainer appContext) throws Exception {
        super(args, appContext);
    }

    @Override
    public void run() throws Exception {
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
                DataObject<String> dataObject = (DataObject<String>) system.newDataObject();
                dataObject.setValues(line);
                data.add( dataObject );
            }

        //Execute
        Timestamp initialTimeStamp = null;
        final AtomicInteger successCounter = new AtomicInteger();
        switch (op) {
            //Insert File
            case "-i" -> {
                initialTimeStamp = new Timestamp(System.currentTimeMillis());
                for (DataObject<String> dataElement : data){
                    //System.out.println("adding:"+ dataElement.getValues());
                    //Execute instruction
                    ListenableFuture<DataObject<?>> result = system.insert(dataElement);
                    Futures.addCallback(result, new FutureCallback<>() {
                        @Override
                        public void onSuccess(DataObject object) {
                            synchronized (successCounter){
                                successCounter.getAndIncrement();
                            }
                            //Complete
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
                //final int[] i = {0};
                initialTimeStamp = new Timestamp(System.currentTimeMillis());
                for (DataObject<String> dataElement : data){

                    //Execute Instruction
                    ListenableFuture<DataObject<?>> result = system.query(dataElement);

                    Futures.addCallback(result, new FutureCallback<>() {
                        @Override
                        public void onSuccess(DataObject object) {
                            synchronized (successCounter){
                                successCounter.getAndIncrement();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Throwable throwable) {
                        }
                    }, appContext.getCallbackExecutor());
                }
            }
        }
        system.stop();
        Timestamp finalTimestamp = new Timestamp(System.currentTimeMillis());
        double totalExecutionTime = (finalTimestamp.getTime() - initialTimeStamp.getTime())/1000f;

        if (successCounter.get() != data.size() )
            throw new Exception("Not all Inserts were performed. Missing: " + (data.size() - successCounter.get()) );

        System.out.println("tet "+ totalExecutionTime +" s");
        System.out.println("t "+ data.size()/totalExecutionTime +" ops/s");
        System.exit(0);
    }
}
