package SystemLayer.Data.UniqueIndentifierImpl;

import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.Data.LSHHashImpl.LSHHashImpl;

import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;

public abstract class UniqueIdentifierImpl implements UniqueIdentifier {

    public byte[] data;
    public DataContainer appContext;

    public UniqueIdentifierImpl( DataContainer appContext ){
        this.appContext = appContext;
    }

    @Override
    public void setObject(byte[] object) {
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

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    //Serialization
    @Serial
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeObject(data);
    }
    @Serial
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        data = (byte[]) stream.readObject();
    }
}
