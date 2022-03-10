package Factories.ComponentFactories;

import SystemLayer.Processes.Nodes.*;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
class NodeFactoryTest {

    @Test
    void getNewInstance_Inserter() {
        NodeFactory.types type = NodeFactory.types.INSERTER;
        Node node = NodeFactory.getNewInstance(type);
        assertEquals( Objects.requireNonNull(node).getClass(), Inserter.class);
    }

    @Test
    void getNewInstance_MultimapServer() {
        NodeFactory.types type = NodeFactory.types.MULTIMAP_SERVER;
        Node node = NodeFactory.getNewInstance(type);
        assertEquals( Objects.requireNonNull(node).getClass(), MultimapServer.class);
    }

    @Test
    void getNewInstance_Querier() {
        NodeFactory.types type = NodeFactory.types.QUERIER;
        Node node = NodeFactory.getNewInstance(type);
        assertEquals( Objects.requireNonNull(node).getClass(), Querier.class);
    }

    @Test
    void getNewInstance_SystemClient() {
        NodeFactory.types type = NodeFactory.types.SYSTEM_CLIENT;
        Node node = NodeFactory.getNewInstance(type);
        assertEquals( Objects.requireNonNull(node).getClass(), SystemClient.class);
    }

}