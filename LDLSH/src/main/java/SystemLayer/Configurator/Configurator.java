package SystemLayer.Configurator;

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

public class Configurator {

    //Configurations
    private String f_name;
    private Properties config;

    //Create Data container
    public Configurator(String f_name){
        try {
            //Open file
            FileReader properties_file = new FileReader(f_name);
            //Load properties
            config = new Properties();
            config.load(properties_file);
            this.f_name = f_name;

        }catch (IOException ioe){
            //If the file can't be opened, creates an empty configurator
            config = new Properties();
            this.f_name = "";
        }
    }

    //getters
    public String getConfig( String config_name ){
        String value = config.getProperty(config_name);
        if( value == null || value.isEmpty() || value.isBlank())
            return "";
        else
            return value;
    }

    //setters
    public void setConfig( String config_name, String config_value ){
        config.setProperty(config_name, config_value);
    }
}
