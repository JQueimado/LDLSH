package SystemLayer.Configurator;

import SystemLayer.Containers.Configurator.Configurator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfiguratorTest {

    String files_path = "src/test/java/SystemLayer/Configurator/";

    //config file tests
    void configFileHelper(String config_name, String expected_val, Configurator configurator){
        String val = configurator.getConfig(config_name);
        assertEquals(val, expected_val);
    }

    @Test
    void configEmptyFileTest() throws Exception {
        String f_name = "empty_configurator_test.properties";
        Configurator configurator = new Configurator(files_path + f_name );

        configFileHelper("NODE",                    "",     configurator);
        configFileHelper("DISTANCE_MEASURER",       "",     configurator);
        configFileHelper("ERASURE_PROCESSOR",       "",     configurator);
        configFileHelper("LSH_HASH",                "",     configurator);
        configFileHelper("PACKER",                  "",     configurator);
        configFileHelper("POST_PROCESSOR",          "",     configurator);
        configFileHelper("UNIQUE_IDENTIFIER",       "",     configurator);
        configFileHelper("COMPLETION_MESSAGE",      "",     configurator);
        configFileHelper("COMPLETION_RESPONSE",     "",     configurator);
        configFileHelper("INSERT_MESSAGE",          "",     configurator);
        configFileHelper("QUERY_MESSAGE",           "",     configurator);
        configFileHelper("QUERY_RESPONSE",          "",     configurator);
        configFileHelper("INSERT_REQUEST",          "",     configurator);
        configFileHelper("INSERT_REQUEST_RESPONSE", "",     configurator);
        configFileHelper("QUERY_REQUEST",           "",     configurator);
        configFileHelper("QUERY_REQUEST_RESPONSE",  "",     configurator);
    }

    @Test
    void configNoneFileTest() throws Exception {
        String f_name = "none_configurator_test.properties";
        Configurator configurator = new Configurator(files_path + f_name );

        configFileHelper("NODE",                    "MULTIMAP_SERVER",      configurator);
        configFileHelper("DISTANCE_MEASURER",       "NONE",                 configurator);
        configFileHelper("ERASURE_PROCESSOR",       "NONE",                 configurator);
        configFileHelper("LSH_HASH",                "NONE",                 configurator);
        configFileHelper("PACKER",                  "NONE",                 configurator);
        configFileHelper("POST_PROCESSOR",          "NONE",                 configurator);
        configFileHelper("UNIQUE_IDENTIFIER",       "NONE",                 configurator);
        configFileHelper("COMPLETION_MESSAGE",      "NONE",                 configurator);
        configFileHelper("COMPLETION_RESPONSE",     "NONE",                 configurator);
        configFileHelper("INSERT_MESSAGE",          "NONE",                 configurator);
        configFileHelper("QUERY_MESSAGE",           "NONE",                 configurator);
        configFileHelper("QUERY_RESPONSE",          "NONE",                 configurator);
        configFileHelper("INSERT_REQUEST",          "NONE",                 configurator);
        configFileHelper("INSERT_REQUEST_RESPONSE", "NONE",                 configurator);
        configFileHelper("QUERY_REQUEST",           "NONE",                 configurator);
        configFileHelper("QUERY_REQUEST_RESPONSE",  "NONE",                 configurator);
    }


    //set config tests
    void setConfigHelper( String config_name, String new_val, Configurator configurator ){
        configurator.setConfig(config_name, new_val);
        String current_val = configurator.getConfig(config_name);
        assertEquals(current_val, new_val);
    }

    @Test
    void setConfigTest() throws Exception{
        String f_name = "none_configurator_test.properties";
        Configurator configurator = new Configurator(files_path + f_name );

        setConfigHelper("NODE",                     "INSERTER",     configurator);
        setConfigHelper("PACKER",                   "STANDARD",     configurator);
        setConfigHelper("POST_PROCESSOR",           "STANDARD",     configurator);
        setConfigHelper("UNIQUE_IDENTIFIER",        "SHA256",       configurator);
        setConfigHelper("COMPLETION_MESSAGE",       "STANDARD",     configurator);
        setConfigHelper("COMPLETION_RESPONSE",      "STANDARD",     configurator);
        setConfigHelper("INSERT_MESSAGE",           "STANDARD",     configurator);
        setConfigHelper("QUERY_MESSAGE",            "STANDARD",     configurator);
        setConfigHelper("QUERY_RESPONSE",           "STANDARD",     configurator);
        setConfigHelper("INSERT_REQUEST",           "STANDARD",     configurator);
        setConfigHelper("INSERT_REQUEST_RESPONSE",  "STANDARD",     configurator);
        setConfigHelper("QUERY_REQUEST",            "STANDARD",     configurator);
        setConfigHelper("QUERY_REQUEST_RESPONSE",   "STANDARD",     configurator);
    }

}