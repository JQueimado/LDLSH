package Factories;

import NetworkLayer.Message;
import NetworkLayer.Messages.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageFactoryTest {

    @Test
    void setConfig() throws Exception{

        String type = "NONE";

        //Completion message
        MessageFactory.setConfig(
                MessageFactory.config_settings.COMPLETION_MESSAGE,
                type
        );
        assertEquals(
                MessageFactory.completionMessageType,
                MessageFactory.completionMessageTypes.NONE
        );

        //completion response
        MessageFactory.setConfig(
                MessageFactory.config_settings.COMPLETION_RESPONSE,
                type
        );
        assertEquals(
                MessageFactory.completionResponseType,
                MessageFactory.completionResponseTypes.NONE
        );

        //insert message
        MessageFactory.setConfig(
                MessageFactory.config_settings.INSERT_MESSAGE,
                type
        );
        assertEquals(
                MessageFactory.insertMessageType,
                MessageFactory.insertMessageTypes.NONE
        );

        //query message
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_MESSAGE,
                type
        );
        assertEquals(
                MessageFactory.queryMessageType,
                MessageFactory.queryMessageTypes.NONE
        );

        //query response
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_RESPONSE,
                type
        );
        assertEquals(
                MessageFactory.queryResponseType,
                MessageFactory.queryResponseTypes.NONE
        );
    }

    @Test
    void newCompletionMessage() throws Exception {

        //No config
        Message new_message = MessageFactory.newCompletionMessage();
        assertNull(new_message);

        //NONE
        String type = "NONE";
        MessageFactory.setConfig(
                MessageFactory.config_settings.COMPLETION_MESSAGE,
                type
        );
        new_message = MessageFactory.newCompletionMessage();
        assertNull(new_message);

        //STANDARD
        type = "STANDARD";
        MessageFactory.setConfig(
                MessageFactory.config_settings.COMPLETION_MESSAGE,
                type
        );
        new_message = MessageFactory.newCompletionMessage();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), CompletionMessage.class);
    }

    @Test
    void newCompletionResponse() throws Exception{

        //No config
        Message new_message = MessageFactory.newCompletionResponse();
        assertNull(new_message);

        //NONE
        String type = "NONE";
        MessageFactory.setConfig(
                MessageFactory.config_settings.COMPLETION_RESPONSE,
                type
        );
        new_message = MessageFactory.newCompletionResponse();
        assertNull(new_message);

        //STANDARD
        type = "STANDARD";
        MessageFactory.setConfig(
                MessageFactory.config_settings.COMPLETION_RESPONSE,
                type
        );
        new_message = MessageFactory.newCompletionResponse();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), CompletionResponse.class);
    }

    @Test
    void newInsertMessage() throws Exception {

        //No config
        Message new_message = MessageFactory.newInsertMessage();
        assertNull(new_message);

        //NONE
        String type = "NONE";
        MessageFactory.setConfig(
                MessageFactory.config_settings.INSERT_MESSAGE,
                type
        );
        new_message = MessageFactory.newInsertMessage();
        assertNull(new_message);

        //STANDARD
        type = "STANDARD";
        MessageFactory.setConfig(
                MessageFactory.config_settings.INSERT_MESSAGE,
                type
        );
        new_message = MessageFactory.newInsertMessage();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), InsertMessage.class);
    }

    @Test
    void newQueryMessage() throws Exception {

        //No config
        Message new_message = MessageFactory.newQueryMessage();
        assertNull(new_message);

        //NONE
        String type = "NONE";
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_MESSAGE,
                type
        );
        new_message = MessageFactory.newQueryMessage();
        assertNull(new_message);

        //SINGLE_BLOCK
        type = "SINGLE_BLOCK";
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_MESSAGE,
                type
        );
        new_message = MessageFactory.newQueryMessage();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), QueryMessageSingleBlock.class);

        //MULTI_BLOCK
        type = "MULTI_BLOCK";
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_MESSAGE,
                type
        );
        new_message = MessageFactory.newQueryMessage();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), QueryMessageMultiBlock.class);
    }

    @Test
    void newQueryResponse() throws Exception {

        //No config
        Message new_message = MessageFactory.newQueryResponse();
        assertNull(new_message);

        //NONE
        String type = "NONE";
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_RESPONSE,
                type
        );
        new_message = MessageFactory.newQueryResponse();
        assertNull(new_message);

        //SINGLE_BLOCK
        type = "STANDARD";
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_RESPONSE,
                type
        );
        new_message = MessageFactory.newQueryResponse();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), QueryResponse.class);

        //MULTI_BLOCK
        type = "OPTIMIZED";
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_RESPONSE,
                type
        );
        new_message = MessageFactory.newQueryResponse();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), QueryResponseOptimized.class);
    }
}