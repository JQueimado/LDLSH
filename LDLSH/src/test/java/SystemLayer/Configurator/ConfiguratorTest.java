package SystemLayer.Configurator;

import Factories.Factory;

import java.io.IOException;

class ConfiguratorTest {

    String files_path = "src/test/java/SystemLayer/Configurator/";

    @org.junit.jupiter.api.Test
    void config_file_not_exists(){
        String test_file = files_path+"empty_configurator_test.properties";
    }

}