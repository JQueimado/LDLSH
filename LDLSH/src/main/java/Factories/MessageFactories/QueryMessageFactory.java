package Factories.MessageFactories;

import NetworkLayer.Messages.QueryMessage;
import Factories.Factory;

public abstract class QueryMessageFactory implements Factory {
    public static QueryMessage newQueryMessage(){
        return null;
    }
}
