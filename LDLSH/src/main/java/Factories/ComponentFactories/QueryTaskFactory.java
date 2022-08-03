package Factories.ComponentFactories;

import Factories.FactoryImpl;
import NetworkLayer.Message;
import SystemLayer.Components.TaskImpl.Worker.Model.ModelOptimizedQueryWorkerTask;
import SystemLayer.Components.TaskImpl.Worker.Model.ModelStandardQueryWorkerTask;
import SystemLayer.Components.TaskImpl.Worker.WorkerTask;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class QueryTaskFactory extends FactoryImpl {
    private static final String config_name = "QUERY_MODE";

    public enum configs {
        STANDARD,
        OPTIMIZED
    }

    public QueryTaskFactory(DataContainer appContext) {
        super(appContext);
    }

    /**
     * Returns a new instance of a DataProcessor object based on the context configuration
     * @return Instance of DataProcessor, null if no configuration is not present
     */
    public WorkerTask getNewQueryTask(Message message) throws Exception {
        String configuration = appContext.getConfigurator().getConfig(config_name);
        return getNewQueryTask(configuration, message);
    }

    /**
     * Returns a new instance of a DataProcessor object based on a given configuration
     * @param configuration Object configuration type
     * @return Instance of DataProcessor configured based on the input, null if no configuration is not present
     */
    public WorkerTask getNewQueryTask(String configuration, Message message) throws Exception {
        try {
            configs config = configs.valueOf(configuration);
            switch (config) {

                case STANDARD -> {
                    return new ModelStandardQueryWorkerTask(message, appContext);
                }

                case OPTIMIZED -> {
                    return new ModelOptimizedQueryWorkerTask(message, appContext);
                }
            }
        }catch (IllegalArgumentException iae){
            throw new UnknownConfigException(config_name, configuration);
        }
        return null;
    }

}
