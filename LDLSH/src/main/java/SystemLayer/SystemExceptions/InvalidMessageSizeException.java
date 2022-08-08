package SystemLayer.SystemExceptions;

public class InvalidMessageSizeException extends Exception{

    public static void handler( InvalidMessageSizeException e ){
        System.err.print(
                "Error: expected message body size:" +
                e.expected_message_size +
                " but got: " +
                e.actual_message_size
        );
    }

    public int expected_message_size;
    public int actual_message_size;

    public InvalidMessageSizeException( int expected_message_size, int actual_message_size ){
        this.expected_message_size = expected_message_size;
        this.actual_message_size = actual_message_size;
    }
}
