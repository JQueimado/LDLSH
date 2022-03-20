package NetworkLayer;

public class MessageImpl implements Message {

    //Message Contents
    private Object body;
    private types type;

    //Constructors
    public MessageImpl( types type, Object body ){
        this.type = type;
        this.body = body;
    }

    public MessageImpl( types type ){
        this( type, null );
    }

    //getters and setters
    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public types getType() {
        return type;
    }

    public void setType(types type) {
        this.type = type;
    }
}
