package NetworkLayer;

import java.io.Serializable;
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
}
