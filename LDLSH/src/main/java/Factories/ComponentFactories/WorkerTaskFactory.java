package Factories.ComponentFactories;

import Factories.FactoryImpl;
import SystemLayer.Components.NetworkLayer.Message;
import SystemLayer.Components.TaskImpl.Worker.Baseline.TraditionalReplicatedInsertTask;
import SystemLayer.Components.TaskImpl.Worker.Baseline.TraditionalReplicatedQueryTask;
import SystemLayer.Components.TaskImpl.Worker.Model.ModelInsertWorkerTask;
import SystemLayer.Components.TaskImpl.Worker.WorkerTask;
import SystemLayer.Containers.DataContainer;
import SystemLayer.SystemExceptions.UnknownConfigException;

public class WorkerTaskFactory extends FactoryImpl {

    private static final String config_name = "TASK_MODEL";
    private enum configs {LDLSH, TRADITIONAL, TRADITIONAL_REPLICATED}

    private WorkerQueryTaskFactory workerQueryTaskFactory = null;

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
            case LDLSH -> {
                return new ModelInsertWorkerTask(message, appContext);
            }

            case TRADITIONAL_REPLICATED -> {
                return new TraditionalReplicatedInsertTask(message, appContext);
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
                if( workerQueryTaskFactory == null )
                    workerQueryTaskFactory = new WorkerQueryTaskFactory(appContext);
                return workerQueryTaskFactory.getNewQueryTask(message);
            }

            case TRADITIONAL_REPLICATED -> {
                return new TraditionalReplicatedQueryTask(message, appContext);
            }

            default -> {
                return null;
            }
        }
    }

}
