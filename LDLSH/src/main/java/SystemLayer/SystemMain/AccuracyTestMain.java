package SystemLayer.SystemMain;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AccuracyTestMain extends SystemMainImp{

    public AccuracyTestMain(String[] args, DataContainer appContext) throws Exception {
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

        AtomicInteger successCounter = new AtomicInteger();

        //Execute
        switch (op) {
            //Insert File
            case "-i" -> {
                for (DataObject<String> dataElement : data){
                    //System.out.println("adding:"+ dataElement.getValues());
                    //Execute instruction
                    ListenableFuture<DataObject<?>> result = system.insert(dataElement);
                    Futures.addCallback(result, new FutureCallback<>() {
                        @Override
                        public void onSuccess(DataObject object) {
                            System.out.println("insert: " + object.getValues());
                            //Nothing
                            synchronized (successCounter) {
                                successCounter.getAndIncrement();
                            }
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
                for (DataObject<?> dataElement : data){

                    //Execute Instruction
                    ListenableFuture<DataObject<?>> result = system.query(dataElement);

                    Futures.addCallback(result, new FutureCallback<>() {
                        final DataObject<?> e = dataElement;
                        @Override
                        public void onSuccess(DataObject object) {
                            if( object != null )
                                System.out.println(e.getValues() + " -> " + object.getValues());
                            else
                                System.out.println(e.getValues() + " -> null" );
                            synchronized (successCounter) {
                                successCounter.getAndIncrement();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Throwable throwable) {
                            System.err.println("ERROR: query failed for item: " + e.getValues());
                            throwable.printStackTrace();
                        }
                    }, appContext.getCallbackExecutor());
                }
            }
        }

        //Shutdown
        system.stop();
        if (successCounter.get() != data.size() )
            throw new Exception("Not all Inserts were performed. Missing: " + (data.size() - successCounter.get()) );

        System.exit(0);
    }
}
