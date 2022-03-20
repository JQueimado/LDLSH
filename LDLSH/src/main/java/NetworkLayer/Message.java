package NetworkLayer;

import java.io.Serializable;

public interface Message extends Serializable {
    enum types{
        NONE,
        COMPLETION_MESSAGE,
        COMPLETION_RESPONSE,
        INSERT_MESSAGE,
        QUERY_MESSAGE_SINGLE_BLOCK,
        QUERY_MESSAGE_MULTI_BLOCK,
        QUERY_RESPONSE,
        QUERY_RESPONSE_OPTIMIZED,
        INSERT_REQUEST,
        INSERT_REQUEST_RESPONSE,
        QUERY_REQUEST,
        QUERY_REQUEST_RESPONSE
    }
}
