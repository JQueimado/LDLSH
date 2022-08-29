package Factories.ComponentFactories;

import Factories.FactoryImpl;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.TaskImpl.Worker.Baseline.TraditionalInsertTask;
import SystemLayer.Components.TaskImpl.Worker.Baseline.TraditionalQueryTask;
import SystemLayer.Components.TaskImpl.Worker.Baseline.TraditionalReplicatedInsertTask;
import SystemLayer.Components.TaskImpl.Worker.Baseline.TraditionalReplicatedQueryTask;
import SystemLayer.Components.TaskImpl.Worker.Model.ModelInsertWorkerTask;
import SystemLayer.Components.TaskImpl.Worker.Model.ModelOptimizedQueryWorkerTask;
import SystemLayer.Components.TaskImpl.Worker.Model.ModelStandardQueryWorkerTask;
import SystemLayer.Components.TaskImpl.Worker.WorkerTask;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class WorkerTaskFactory extends FactoryImpl {

    private static final String config_name = "WORKER_TASK_MODEL";
    private enum configs {
        LDLSH,
        LDLSH_OPTIMIZED,
        TRADITIONAL,
        TRADITIONAL_REPLICATED
    }

    public WorkerTaskFactory(DataContainer appContext){
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
    public WorkerTask getNewWorkerInserterTask(Message message) throws Exception {
        configs config = getConfig();

        switch (config){
            case LDLSH, LDLSH_OPTIMIZED -> {
                return new ModelInsertWorkerTask(message, appContext);
            }

            case TRADITIONAL_REPLICATED -> {
                return new TraditionalReplicatedInsertTask(message, appContext);
            }

            case TRADITIONAL -> {
                return new TraditionalInsertTask(message, appContext);
            }

            default -> {
                return null;
            }
        }
    }

    //WorkerQueryTask
    public WorkerTask getNewWorkerQueryTask(Message message) throws Exception {
        configs config = getConfig();

        switch (config){
            case LDLSH -> {
                return new ModelStandardQueryWorkerTask(message, appContext);
            }

            case LDLSH_OPTIMIZED -> {
                return new ModelOptimizedQueryWorkerTask(message, appContext);
            }

            case TRADITIONAL_REPLICATED -> {
                return new TraditionalReplicatedQueryTask(message, appContext);
            }

            case TRADITIONAL -> {
                return new TraditionalQueryTask(message, appContext);
            }

            default -> {
                return null;
            }
        }
    }

}
