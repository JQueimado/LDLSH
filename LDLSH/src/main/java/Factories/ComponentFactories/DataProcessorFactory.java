package Factories.ComponentFactories;

import Factories.FactoryImpl;
import SystemLayer.Components.DataProcessor.DataProcessor;
import SystemLayer.Components.DataProcessor.SecretShareDataProcessor;
import SystemLayer.Components.DataProcessor.StandardDataProcessor;
import SystemLayer.Containers.DataContainer;

public class DataProcessorFactory extends FactoryImpl {

    private static final String config_name = "DATA_PROCESSOR";

    public enum configs {
        STANDARD,
        SECRETE_SHARE
    }

    public DataProcessorFactory(DataContainer appContext) {
        super(appContext);
    }

    /**
     * Returns a new instance of a DataProcessor object based on the context configuration
     * @return Instance of DataProcessor, null if no configuration is not present
     */
    public DataProcessor getNewDataProcessor(){
        String configuration = appContext.getConfigurator().getConfig(config_name);
        return getNewDataProcessor(configuration);
    }

    /**
     * Returns a new instance of a DataProcessor object based on a given configuration
     * @param configuration Object configuration type
     * @return Instance of DataProcessor configured based on the input, null if no configuration is not present
     */
    public DataProcessor getNewDataProcessor( String configuration ){
        configs config = configs.valueOf(configuration);
        switch (config){

            case STANDARD -> {
                return new StandardDataProcessor(appContext);
            }

            case SECRETE_SHARE -> {
                return new SecretShareDataProcessor(appContext);
            }

            default -> {
                return null;
            }
        }
    }

}
