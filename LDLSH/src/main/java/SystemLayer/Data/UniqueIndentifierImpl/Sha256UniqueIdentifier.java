package SystemLayer.Data.UniqueIndentifierImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import com.google.common.hash.Hashing;

import java.util.Arrays;

public class Sha256UniqueIdentifier extends UniqueIdentifierImpl{

    public Sha256UniqueIdentifier(DataContainer appContext){
        super(appContext);
    }

    @Override
    public void setObject(byte[] object) {
        this.data = Hashing.sha256().hashBytes(object).asBytes();
    }
}
