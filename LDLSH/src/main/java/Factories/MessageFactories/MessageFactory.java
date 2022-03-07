package Factories.MessageFactories;

import NetworkLayer.Message;
import NetworkLayer.Messages.*;

public abstract class MessageFactory {
    /* Configs */
    public enum completionMessageTypes {STANDARD}
    public enum completionResponseTypes {STANDARD}
    public enum insertMessageTypes {STANDARD}
    public enum queryMessageTypes {SINGLE_BLOCK, MULTI_BLOCK}
    public enum queryResponseTypes {STANDARD, OPTIMIZED}


    //Current config
    public static completionMessageTypes completionMessageType;
    public static completionResponseTypes completionResponseType;
    public static insertMessageTypes insertMessageType;
    public static queryMessageTypes queryMessageType;
    public static queryResponseTypes queryResponseType;


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
