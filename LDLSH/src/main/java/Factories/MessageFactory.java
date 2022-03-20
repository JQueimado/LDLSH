package Factories;

import NetworkLayer.Message;
import NetworkLayer.MessageImpl;

public class MessageFactory implements Factory {

    public MessageFactory(){
        /**/
    }

    public Message getMessage( Message.types type ){
        return new MessageImpl(type);
    }

}
