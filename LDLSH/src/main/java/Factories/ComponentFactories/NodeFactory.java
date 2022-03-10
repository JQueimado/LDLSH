package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Processes.Nodes.*;

import java.security.PublicKey;

public abstract class NodeFactory implements Factory {

    public enum types {INSERTER, MULTIMAP_SERVER, QUERIER, SYSTEM_CLIENT}

    public static Node getNewInstance(types nodeType){
        switch (nodeType){
            case INSERTER -> {
                return new Inserter();
            }
            case MULTIMAP_SERVER -> {
                return new MultimapServer();
            }
            case QUERIER -> {
                return new Querier();
            }
            case SYSTEM_CLIENT -> {
                return new SystemClient();
            }

            default -> {
                return null;
            }
        }
    }
}
