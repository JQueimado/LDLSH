package SystemLayer.Data.UniqueIndentifierImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.util.Arrays;

public abstract class UniqueIdentifierImpl implements UniqueIdentifier {

    public byte[] data;
    public DataContainer appContext;

    public UniqueIdentifierImpl( DataContainer appContext ){
        this.appContext = appContext;
    }

    @Override
    public void setObject(DataObject dataObject) {

    }

    @Override
    public byte[] getUID() {
        return data;
    }

    @Override
    public int compareTo(UniqueIdentifier o) {
        return Arrays.compare(data, o.getUID());
    }

    @Override
    public boolean equals(Object obj) {
        return this.compareTo((UniqueIdentifier) obj) == 0;
    }
}
