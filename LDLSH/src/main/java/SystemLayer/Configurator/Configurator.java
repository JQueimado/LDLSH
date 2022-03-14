package SystemLayer.Configurator;

import Factories.ComponentFactories.*;
import Factories.Factory;
import Factories.MessageFactory;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/*
* Properties:
*
* - DISTANCE_MEASURER
*       * Description: Configures the distance metric used to compare objects.
*       * Options: NONE, STANDARD
*
* - ERASURE_PROCESSOR
*       * Description: Configures the algorithm used to process erasure codes.
*       * Options: NONE, REED_SOLOMON
*
* - LSH_HASH
*       * Description: Configures the LSH hash function to be used.
*       * Options: NONE, STANDARD
*
* - PACKER
*       * Description: Configures what algorithm to be used in the packer process
*       * Options: NONE, STANDARD
*
* - POST_PROCESSOR
*       * Description:
*       * Options:
*
* - UNIQUE_IDENTIFIER
*       * Description:
*       * Options:
*
* - Messages:
*       * COMPLETION_MESSAGE:
*           ** description: Sets the completion message implementation
*           ** options: NONE, STANDARD
*       * COMPLETION_RESPONSE:
*           ** description: Sets the completion response message implementation
*           ** options: NONE, STANDARD
*       * INSERT_MESSAGE:
*           ** description: Sets the insert message implementation
*           ** options: NONE, STANDARD
*       * QUERY_MESSAGE:
*           ** description: Sets the query message implementation
*           ** options: NONE, SINGLE_BLOCK, MULTI_BLOCK
*       * QUERY_RESPONSE:
*           ** description: Sets the query response message implementation
*           ** options: NONE, STANDARD, OPTIMIZED
* */

public abstract class Configurator {
    public static void config(String file_name) throws IOException, Factory.ConfigException {
        //Open file
        FileReader properties_file = new FileReader(file_name);

        //Load properties
        Properties p = new Properties();
        p.load(properties_file);

        //config Components
        //Distance measure
        DistanceMeasurerFactory.setCurrentType(p.getProperty("DISTANCE_MEASURE"));

        //Erasure codes
        ErasureProcessorFactory.setCurrentType(p.getProperty("ERASURE_PROCESSOR"));

        //LSH hash
        LSHHashProcessorFactory.setCurrentType(p.getProperty("LSH_HASH"));

        //Packer
        PackerFactory.setCurrentType(p.getProperty("PACKER"));

        //Post processor
        PostProcessorFactory.setCurrentType(p.getProperty("POST_PROCESSOR"));

        //Unique identifier
        UniqueIdentifierProcessorFactory.setCurrentType(p.getProperty("UNIQUE_IDENTIFIER"));

        //config Messages
        //Completion message
        MessageFactory.setConfig(
                MessageFactory.config_settings.COMPLETION_MESSAGE,
                p.getProperty("COMPLETION_MESSAGE")
        );

        //Completion response
        MessageFactory.setConfig(
                MessageFactory.config_settings.COMPLETION_RESPONSE,
                p.getProperty("COMPLETION_RESPONSE")
        );

        //Insert message
        MessageFactory.setConfig(
                MessageFactory.config_settings.INSERT_MESSAGE,
                p.getProperty("INSERT_MESSAGE")
        );

        //Query message
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_MESSAGE,
                p.getProperty("QUERY_MESSAGE")
        );

        //Query response
        MessageFactory.setConfig(
                MessageFactory.config_settings.QUERY_RESPONSE,
                p.getProperty("QUERY_RESPONSE")
        );
    }
}
