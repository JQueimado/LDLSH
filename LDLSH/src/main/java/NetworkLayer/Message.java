package NetworkLayer;

import java.io.Serializable;
import java.util.List;

public interface Message extends Serializable {

    enum types{
        NONE,
        COMPLETION_MESSAGE,
        COMPLETION_RESPONSE,
        INSERT_MESSAGE,
        INSERT_MESSAGE_RESPONSE,
        QUERY_MESSAGE_SINGLE_BLOCK,
        QUERY_MESSAGE_MULTI_BLOCK,
        QUERY_RESPONSE,
        QUERY_RESPONSE_OPTIMIZED,
        INSERT_REQUEST,
        INSERT_REQUEST_RESPONSE,
        QUERY_REQUEST,
        QUERY_REQUEST_RESPONSE
    }

    /**
     * Returns the message's transaction id
     * @return id
     */
    int getTransactionId();

    /**
     * Sets the message's transaction id
     * @param transactionId id
     */
    void setTransactionId( int transactionId );

    /**
     * Returns the message's body
     * @return message body as an object
     */
    List<Object> getBody();

    /**
     * Stets the message body value
     * @param body body value
     */
    void setBody(List<Object> body);

    /**
     * Returns the message value
     * @return message type
     */
    types getType();

    /**
     * Set the message type
     * @param type message type
     */
    void setType(types type);
}
