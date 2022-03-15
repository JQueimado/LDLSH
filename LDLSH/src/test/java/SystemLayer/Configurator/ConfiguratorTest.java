package SystemLayer.Configurator;

import Factories.ComponentFactories.DistanceMeasurerFactory;
import Factories.Factory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfiguratorTest {

    String files_path = "src/test/java/SystemLayer/Configurator/";

    @org.junit.jupiter.api.Test
    void config_file_not_exists() throws Exception{
        String test_file = files_path+"empty_configurator_test.properties";
        Configurator.config(test_file);
        assertEquals(
                DistanceMeasurerFactory.current_type,
                DistanceMeasurerFactory.types.NONE
        );
    }

    @Test
    void config_none_file(){
        String test_file = files_path+"NONE_configurator_test.properties";

    }

}