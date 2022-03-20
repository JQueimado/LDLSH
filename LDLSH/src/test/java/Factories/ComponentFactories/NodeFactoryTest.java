package Factories.ComponentFactories;

import SystemLayer.Processes.Nodes.*;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
class NodeFactoryTest {

    @Test
    void getNewInstance_Inserter() {
        String type = "INSERTER";
        NodeFactory nodeFactory = new NodeFactory();
        Node node = nodeFactory.getNode(type);
        assertNotNull( node );
        assertEquals( node.getClass(), Inserter.class);
    }

    @Test
    void getNewInstance_MultimapServer() {
        String type = "MULTIMAP_SERVER";
        NodeFactory nodeFactory = new NodeFactory();
        Node node = nodeFactory.getNode(type);
        assertNotNull( node );
        assertEquals( node.getClass(), MultimapServer.class);
    }

    @Test
    void getNewInstance_Querier() {
        String type = "QUERIER";
        NodeFactory nodeFactory = new NodeFactory();
        Node node = nodeFactory.getNode(type);
        assertNotNull( node );
        assertEquals( node.getClass(), Querier.class);
    }

    @Test
    void getNewInstance_SystemClient() {
        String type = "SYSTEM_CLIENT";
        NodeFactory nodeFactory = new NodeFactory();
        Node node = nodeFactory.getNode(type);
        assertNotNull( node );
        assertEquals( node.getClass(), SystemClient.class);
    }

}