package SystemLayer.Components.System;

import Factories.ComponentFactories.MultimapFactory;
import NetworkLayer.Message;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.InsertTask;
import SystemLayer.Components.TaskImpl.StandardQueryTask;
import SystemLayer.Components.TaskImpl.Task;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CentralizedSystem implements System {

    private DataContainer context;

    public CentralizedSystem( String configFile ) throws Exception {
        context = new DataContainer(configFile);

        //Setup
        Configurator configurator = context.getConfigurator();
        int bands = Integer.parseInt( configurator.getConfig("N_BANDS") );

        //-MultiMaps
        MultimapFactory multimapFactory = new MultimapFactory();
        String multimapConfig = configurator.getConfig("MULTIMAP");
        MultiMap[] multiMaps = new MultiMap[bands];
        for ( int i = 0; i<bands; i++ ){
            multiMaps[i] = multimapFactory.getNewMultiMap(multimapConfig);
        }
        context.setMultiMaps(multiMaps);

        //-Executor Service
        int threads = Integer.parseInt( configurator.getConfig("N_THREADS") );
        ExecutorService executorService = Executors.newFixedThreadPool( threads );
        context.setExecutorService(executorService);

    }

    @Override
    public void insert(DataObject object) throws Exception {
        Message insertMessage = context.getMessageFactory().getMessage( Message.types.INSERT_REQUEST );
        insertMessage.setBody( object );
        Task insertTask = new InsertTask(insertMessage, context );
        context.getExecutorService().submit( insertTask );
    }

    @Override
    public DataObject query(DataObject queryObject) throws Exception {
        Message queryMessage = context.getMessageFactory().getMessage( Message.types.QUERY_REQUEST );
        queryMessage.setBody( queryObject );
        Task queryTask = new StandardQueryTask(queryMessage, context );
        Future<DataObject> response = context.getExecutorService().submit( queryTask );
        return response.get();
    }
}
