package Factories.RequestFactories;

import NetworkLayer.Requests.RequestQuery;
import Factories.Factory;

public abstract class RequestQueryFactory implements Factory {
    public static RequestQuery newQueryRequest(){
        return null;
    }
}
