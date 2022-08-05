package SystemLayer.SystemExceptions;

public class InvalidMessageBodyObjectException extends Exception{

    public static void handler( InvalidMessageBodyObjectException e ){
        System.err.print(
                "Error: expected body object of type:" +
                e.expected_class +
                " but got: " +
                e.actual_class
        );
    }

    public String expected_class;
    public String actual_class;

    public InvalidMessageBodyObjectException( String expected_class, String actual_class ){
        this.expected_class = expected_class;
        this.actual_class = actual_class;
    }
}
