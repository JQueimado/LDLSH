package Factories.ComponentFactories;

import Factories.Factory;
import SystemLayer.Processes.Nodes.*;

public class NodeFactory implements Factory {

    public enum configurations {INSERTER, MULTIMAP_SERVER, QUERIER, SYSTEM_CLIENT}

    public NodeFactory(){/**/}

    public Node getNode(String config_name){
        configurations config = configurations.valueOf(config_name);
        switch (config){
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
