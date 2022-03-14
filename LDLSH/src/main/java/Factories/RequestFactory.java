package Factories.RequestFactories;

import NetworkLayer.Request;
import NetworkLayer.Requests.RequestInsert;
import NetworkLayer.Requests.RequestQuery;

public abstract class RequestFactory {

    /* Configs */
    public enum requestInsertTypes {STANDARD}
    public enum requestQueryTypes {STANDARD}
    public enum requestQueryResponseTypes {STANDARD}

    //Current config
    public static requestInsertTypes requestInsertType;
    public static requestQueryTypes requestQueryType;
    public static requestQueryResponseTypes requestQueryResponseType;

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
                return new RequestQuery();
            }

            default -> {
                return null;
            }
        }
    }

}
