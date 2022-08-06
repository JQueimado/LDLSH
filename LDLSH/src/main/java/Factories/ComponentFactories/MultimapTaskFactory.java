package Factories.ComponentFactories;

import Factories.FactoryImpl;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.TaskImpl.Multimap.CompletionMultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.InsertMultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.MultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.QueryMultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.TraditionalTasks.TraditionalInsertMultimapTask;
import SystemLayer.Components.TaskImpl.Multimap.TraditionalTasks.TraditionalQueryMultimapTask;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class MultimapTaskFactory extends FactoryImpl {

    private static final String config_name = "WORKER_TASK_MODEL";
    private enum configs {NONE, LDLSH, TRADITIONAL, TRADITIONAL_REPLICATED}

    private WorkerQueryTaskFactory workerQueryTaskFactory = null;

    public MultimapTaskFactory(DataContainer appContext){
        super(appContext);
    }

    private configs getConfig() throws UnknownConfigException {
        String config_value = "";
        try {
            config_value = appContext.getConfigurator().getConfig(config_name);
            return configs.valueOf(config_value);
        }catch (Exception e){
            throw new UnknownConfigException(config_name, config_value);
        }
    }

    //WorkerInsert
    public MultimapTask getNewMultimapInserterTask(Message message) throws Exception {
        configs config = getConfig();

        switch (config){
            case LDLSH, TRADITIONAL_REPLICATED -> {
                return new InsertMultimapTask(message, appContext);
            }

            case TRADITIONAL -> {
                return new TraditionalInsertMultimapTask(message, appContext);
            }

            default -> {
                return null;
            }
        }
    }

    //WorkerQueryTask
    public MultimapTask getNewMultimapQueryTask(Message message) throws Exception {
        configs config = getConfig();

        switch (config){
            case LDLSH, TRADITIONAL_REPLICATED -> {
                return new QueryMultimapTask(message, appContext);
            }

            case TRADITIONAL -> {
                return new TraditionalQueryMultimapTask(message, appContext);
            }

            default -> {
                return null;
            }
        }
    }

    public MultimapTask getNewCompletionQueryTask(Message message) throws Exception {
        configs config = getConfig();

        switch (config){
            case LDLSH -> {
                return new CompletionMultimapTask(message, appContext);
            }

            default -> {
                return null;
            }
        }
    }

}
