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

public class MemoryTestMain1 extends SystemMainImp{

    public MemoryTestMain1(String[] args, DataContainer appContext) throws Exception {
        super(args, appContext);
    }


    // Insert objects into a List until it blows up
    //  -> file: data file name

    @Override
    public void run() throws Exception {
        String fileName = args[1];

        //LoadFile
        AtomicInteger atomicInteger = new AtomicInteger();
        List<DataObject<String>> data = new ArrayList<>();
        BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName));
        String line;
        while((line = fileBufferReader.readLine()) != null)
            if (!line.isEmpty() || !line.isBlank()) {
                DataObject<String> dataObject = (DataObject<String>) system.newDataObject();
                dataObject.setValues(line);
                data.add( dataObject );
                System.out.println("Insert nº "+ atomicInteger.getAndIncrement());
            }
    }
}
