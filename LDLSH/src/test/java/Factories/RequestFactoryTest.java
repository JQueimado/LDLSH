package Factories;

import NetworkLayer.Request;
import NetworkLayer.Requests.RequestInsert;
import NetworkLayer.Requests.RequestQuery;
import NetworkLayer.Requests.RequestQueryResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestFactoryTest {

    @BeforeEach
    void before(){
        RequestFactory.requestInsertType = RequestFactory.requestInsertTypes.NONE;
        RequestFactory.requestQueryType = RequestFactory.requestQueryTypes.NONE;
        RequestFactory.requestQueryResponseType = RequestFactory.requestQueryResponseTypes.NONE;
    }

    @Test
    void setConfig() throws Exception {
        String type = "STANDARD";

        //Insert
        RequestFactory.setConfig(
                RequestFactory.config_settings.INSERT_REQUEST,
                type
        );
        assertEquals(
                RequestFactory.requestInsertType,
                RequestFactory.requestInsertTypes.STANDARD
        );

        //Query
        RequestFactory.setConfig(
                RequestFactory.config_settings.QUERY_REQUEST,
                type
        );
        assertEquals(
                RequestFactory.requestQueryType,
                RequestFactory.requestQueryTypes.STANDARD
        );

        //Query Response
        RequestFactory.setConfig(
                RequestFactory.config_settings.QUERY_RESPONSE,
                type
        );
        assertEquals(
                RequestFactory.requestQueryResponseType,
                RequestFactory.requestQueryResponseTypes.STANDARD
        );

    }

    @Test
    void newInsertRequest() throws Exception {

        //No config
        Request new_message = RequestFactory.newInsertRequest();
        assertNull(new_message);

        //NONE
        String type = "NONE";
        RequestFactory.setConfig(
                RequestFactory.config_settings.INSERT_REQUEST,
                type
        );
        new_message = RequestFactory.newInsertRequest();
        assertNull(new_message);

        //STANDARD
        type = "STANDARD";
        RequestFactory.setConfig(
                RequestFactory.config_settings.INSERT_REQUEST,
                type
        );
        new_message = RequestFactory.newInsertRequest();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), RequestInsert.class);
    }

    @Test
    void newQueryRequest() throws Exception {
        //No config
        Request new_message = RequestFactory.newQueryRequest();
        assertNull(new_message);

        //NONE
        String type = "NONE";
        RequestFactory.setConfig(
                RequestFactory.config_settings.QUERY_REQUEST,
                type
        );
        new_message = RequestFactory.newQueryRequest();
        assertNull(new_message);

        //STANDARD
        type = "STANDARD";
        RequestFactory.setConfig(
                RequestFactory.config_settings.QUERY_REQUEST,
                type
        );
        new_message = RequestFactory.newQueryRequest();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), RequestQuery.class);
    }

    @Test
    void newRequestQueryResponse() throws Exception {
        //No config
        Request new_message = RequestFactory.newRequestQueryResponse();
        assertNull(new_message);

        //NONE
        String type = "NONE";
        RequestFactory.setConfig(
                RequestFactory.config_settings.QUERY_RESPONSE,
                type
        );
        new_message = RequestFactory.newRequestQueryResponse();
        assertNull(new_message);

        //STANDARD
        type = "STANDARD";
        RequestFactory.setConfig(
                RequestFactory.config_settings.QUERY_RESPONSE,
                type
        );
        new_message = RequestFactory.newRequestQueryResponse();
        assertNotNull(new_message);
        assertEquals( new_message.getClass(), RequestQueryResponse.class);
    }
}