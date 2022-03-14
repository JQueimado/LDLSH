package Factories;

import NetworkLayer.Request;
import NetworkLayer.Requests.RequestInsert;
import NetworkLayer.Requests.RequestQuery;
import NetworkLayer.Requests.RequestQueryResponse;

public abstract class RequestFactory {

    //Config function settings
    public enum config_settings {
        INSERT_REQUEST,
        QUERY_REQUEST,
        QUERY_RESPONSE
    }

    /* Configs */
    public enum requestInsertTypes {NONE,STANDARD}
    public enum requestQueryTypes {NONE,STANDARD}
    public enum requestQueryResponseTypes {NONE,STANDARD}

    //Current config
    public static requestInsertTypes requestInsertType = requestInsertTypes.NONE;
    public static requestQueryTypes requestQueryType = requestQueryTypes.NONE;
    public static requestQueryResponseTypes requestQueryResponseType = requestQueryResponseTypes.NONE;

    public static void setConfig( config_settings config_name, String config_value ) throws Factory.ConfigException {
        try {
            switch (config_name) {

                case INSERT_REQUEST -> requestInsertType = requestInsertTypes.valueOf(config_value);
                case QUERY_REQUEST -> requestQueryType = requestQueryTypes.valueOf(config_value);
                case QUERY_RESPONSE -> requestQueryResponseType = requestQueryResponseTypes.valueOf(config_value);

                default ->
                        throw new Factory.ConfigException("invalid config_setting", config_name.toString(), config_value );

            }
        }catch ( IllegalArgumentException iae ){
            throw new Factory.ConfigException(
                    "Invalid specification for the specified config_exception",
                    config_name.toString(),
                    config_value
            );
        }
    }

    //Factories
    //Insert Request
    public static Request newInsertRequest(){
        switch (requestInsertType){

            case STANDARD -> {
                return new RequestInsert();
            }

            default -> {
                return null;
            }
        }
    }

    //Query Request
    public static Request newQueryRequest(){
        switch (requestQueryType){

            case STANDARD -> {
                return new RequestQuery();
            }

            default -> {
                return null;
            }
        }
    }

    //Query Request
    public static Request newRequestQueryResponse(){
        switch (requestQueryResponseType){

            case STANDARD -> {
                return new RequestQueryResponse( null );
            }

            default -> {
                return null;
            }
        }
    }

}
