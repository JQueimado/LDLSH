package Factories;

import NetworkLayer.Message;
import NetworkLayer.Messages.*;

public abstract class MessageFactory implements Factory {

    //Config function settings
    public enum config_settings {
        COMPLETION_MESSAGE,
        COMPLETION_RESPONSE,
        INSERT_MESSAGE,
        QUERY_MESSAGE,
        QUERY_RESPONSE
    }

    /* Configs */
    public enum completionMessageTypes {NONE,STANDARD}
    public enum completionResponseTypes {NONE,STANDARD}
    public enum insertMessageTypes {NONE,STANDARD}
    public enum queryMessageTypes {NONE,SINGLE_BLOCK, MULTI_BLOCK}
    public enum queryResponseTypes {NONE,STANDARD, OPTIMIZED}


    //Current config
    public static completionMessageTypes completionMessageType = completionMessageTypes.NONE;
    public static completionResponseTypes completionResponseType = completionResponseTypes.NONE;
    public static insertMessageTypes insertMessageType = insertMessageTypes.NONE;
    public static queryMessageTypes queryMessageType = queryMessageTypes.NONE;
    public static queryResponseTypes queryResponseType = queryResponseTypes.NONE;

    //Set config
    public static void setConfig( config_settings config_name, String config_value )throws ConfigException{
        try {
            switch (config_name) {

                case COMPLETION_MESSAGE -> completionMessageType = completionMessageTypes.valueOf(config_value);

                case COMPLETION_RESPONSE -> completionResponseType = completionResponseTypes.valueOf(config_value);

                case INSERT_MESSAGE -> insertMessageType = insertMessageTypes.valueOf(config_value);

                case QUERY_MESSAGE -> queryMessageType = queryMessageTypes.valueOf(config_value);

                case QUERY_RESPONSE -> queryResponseType = queryResponseTypes.valueOf(config_value);

                default ->
                    throw new ConfigException("invalid config_setting", config_name.toString(), config_value );

            }
        }catch ( IllegalArgumentException iae ){
            throw new ConfigException(
                    "Invalid specification for the specified config_exception",
                    config_name.toString(),
                    config_value
            );
        }
    }

    //Completion Factory
    public static Message newCompletionMessage(){

        switch (completionMessageType){

            // Standard
            case STANDARD -> {
                return new CompletionMessage(null, null);
            }

            // No config
            default -> {
                return null;
            }
        }
    }

    //CompletionResponse
    public static Message newCompletionResponse(){

        switch (completionResponseType){

            // Standard
            case STANDARD -> {
                return new CompletionResponse(null);
            }

            // No config
            default -> {
                return null;
            }
        }
    }

    //InsertMessage
    public static Message newInsertMessage(){

        switch (insertMessageType){

            // Standard
            case STANDARD -> {
                return new InsertMessage(null);
            }

            // No config
            default -> {
                return null;
            }
        }
    }

    //QueryMessage
    public static Message newQueryMessage(){

        switch (queryMessageType){

            // Standard
            case SINGLE_BLOCK -> {
                return new QueryMessageSingleBlock(null);
            }

            case MULTI_BLOCK -> {
                return new QueryMessageMultiBlock(null);
            }

            // No config
            default -> {
                return null;
            }
        }
    }

    //QueryResponse
    public static Message newQueryResponse(){

        switch (queryResponseType){

            // Standard
            case STANDARD -> {
                return new QueryResponse(null,null);
            }

            case OPTIMIZED -> {
                return new QueryResponseOptimized(null);
            }

            // No config
            default -> {
                return null;
            }
        }
    }
}
