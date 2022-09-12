package SystemLayer.Components.MultiMapImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataUnits.LSHHashBlock;
import SystemLayer.Data.DataUnits.ModelMultimapValue;
import SystemLayer.Data.DataUnits.MultiMapValue;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;
import SystemLayer.SystemExceptions.InvalidMapValueTypeException;
import SystemLayer.SystemExceptions.UnknownConfigException;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SemiPersistentMultiMap extends MultiMapImpl {

    private static final String folder_config = "STORAGE_DIR";

    private final AtomicInteger fileNames;
    private final HashMap<LSHHashBlock, Integer> map;
    private String folder_name;

    public SemiPersistentMultiMap(DataContainer appContext) {
        super(appContext);

        //init
        fileNames = new AtomicInteger();
        map = new HashMap<>();

        //Get folder name
        folder_name = appContext.getConfigurator().getConfig(folder_config);
        if(folder_name.isEmpty() || folder_name.isBlank())
            folder_name = "./data";

        //Create folder
        try {
            File temp = new File(folder_name);
            temp.mkdirs();
        }catch (Exception e){
            UnknownConfigException.handler( new UnknownConfigException(folder_config, folder_name));
        }
    }

    private Set<MultiMapValue> readFile( Integer n ) throws Exception{
        FileInputStream fin = new FileInputStream(folder_name + "/" + n + ".bin" );
        ObjectInputStream ois = new ObjectInputStream(fin);
        Object raw_set = ois.readObject();
        ois.close();
        fin.close();

        if( !(raw_set instanceof Set<?>) )
            throw new Exception("Wrong file contents");

        return (Set<MultiMapValue>) raw_set;
    }

    private void writeFile( Set set, Integer n ) throws Exception{
        FileOutputStream fos = new FileOutputStream( folder_name + "/" + n + ".bin" );
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(set);
        oos.close();
        fos.close();
    }

    @Override
    public boolean insert(LSHHash lshHash, MultiMapValue value) throws Exception {

        Integer n = map.get(lshHash.getBlockAt(hash_position));

        if( n == null){
            //no file
            synchronized (fileNames){
                n = fileNames.getAndIncrement();

                Set<MultiMapValue> set = new HashSet<>();
                set.add(value);

                writeFile(set, n);

                map.put(lshHash.getBlockAt(hash_position), n);
            }
        }else {

            synchronized (n){
                //read
                Set<MultiMapValue> set = readFile(n);

                //add
                set.add(value);

                //write
                writeFile(set, n);
            }

        }

        return true;
    }

    @Override
    public ErasureBlock complete(LSHHash lshHash, UniqueIdentifier uniqueIdentifier) throws Exception {
        Integer n = map.get(lshHash.getBlockAt(hash_position));
        if( n == null )
            return null;

        //read
        Set<MultiMapValue> set = readFile(n);

        //search
        for(MultiMapValue value: set ){
            if( !(value instanceof ModelMultimapValue temp) )
                throw new InvalidMapValueTypeException("Expected ModelMultimap but got" + value.getClass());

            if( temp.uniqueIdentifier().equals(uniqueIdentifier) )
                return temp.erasureCode();
        }

        return null;
    }

    @Override
    public MultiMapValue[] query(LSHHashBlock lshHash) throws Exception {

        Integer n = map.get(lshHash);
        if( n == null )
            return null;

        //add
        Set<MultiMapValue> set = readFile(n);

        return set.toArray(new MultiMapValue[0]);

    }
}
