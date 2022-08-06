package SystemLayer.Components.NetworkLayer;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

public class MessageImpl implements Message {

    //Message Contents
    private int transaction_id;
    private List<Object> body;
    private types type;

    //Constructors
    public MessageImpl( types type, List<Object> body ){
        this.setType(type);
        this.setBody(body);
    }

    @Override
    public int getTransactionId() {
        return transaction_id;
    }

    @Override
    public void setTransactionId(int transactionId) {
        this.transaction_id = transactionId;
    }

    //getters and setters
    public List<Object> getBody() {
        return body;
    }

    public void setBody(List<Object> body) {
        this.body = body;
    }

    public types getType() {
        return type;
    }

    public void setType(types type) {
        this.type = type;
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeInt(  getTransactionId() );
        stream.writeInt( getType().ordinal() );
        stream.writeObject( getBody() );
    }

    @Serial
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        setTransactionId( stream.readInt() );
        setType( types.values()[ stream.readInt() ] );
        setBody( (List<Object>) stream.readObject() );
    }
}
