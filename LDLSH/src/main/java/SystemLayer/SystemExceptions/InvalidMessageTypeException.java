package SystemLayer.SystemExceptions;

import SystemLayer.Components.NetworkLayer.Message;

public class InvalidMessageTypeException extends Exception{

    public static void handler( InvalidMessageTypeException e ){
        System.err.print(
                "Error: expected message type:" +
                e.expected_message_type.name() +
                " but got type: " +
                e.actual_message_type.name()
                );
    }

    public Message.types expected_message_type;
    public Message.types actual_message_type;

    public InvalidMessageTypeException(
            Message.types expected_message_type,
           Message.types actual_message_type ){
        this.expected_message_type = expected_message_type;
        this.actual_message_type = actual_message_type;
    }
}
