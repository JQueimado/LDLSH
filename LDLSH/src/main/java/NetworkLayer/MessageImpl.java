package NetworkLayer;

import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageImpl implements Message {

    //Message Contents
    private List<Object> body;
    private types type;

    //Constructors
    public MessageImpl( types type, List<Object> body ){
        this.setType(type);
        this.setBody(body);
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
        stream.writeInt( type.ordinal() );
        stream.writeObject( body );
        //for( Object object : body )
        //    stream.writeObject(object);
    }

    @Serial
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        type = types.values()[ stream.readInt() ];
        body = (List<Object>) stream.readObject();
        //body = new ArrayList<>();
        //while (stream. > 0)
        //    body.add( stream.readObject() );
    }
}
