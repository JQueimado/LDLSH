package SystemLayer.Data.UniqueIndentifierImpl;

import SystemLayer.Data.DataObjectsImpl.DataObject;
import com.google.common.hash.Hashing;

import java.util.Arrays;

public class Sha256UniqueIdentifier implements UniqueIdentifier{

    private byte[] ui;

    public Sha256UniqueIdentifier( DataObject object ){
        setObject(object);
    }

    public Sha256UniqueIdentifier(){
        ui = null;
    }

    @Override
    public void setObject(DataObject dataObject) {
        this.ui = Hashing.sha256().hashBytes(dataObject.toByteArray()).asBytes();
    }

    @Override
    public byte[] getUID() {
        return ui;
    }

    @Override
    public int compareTo( UniqueIdentifier o) {
        return Arrays.compare(this.getUID(), o.getUID());
    }
}
