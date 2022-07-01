package SystemLayer.Components.SystemServer;

import Factories.ComponentFactories.MultimapFactory;
import NetworkLayer.Message;
import NetworkLayer.MessageImpl;
import SystemLayer.Components.MultiMapImpl.MultiMap;
import SystemLayer.Components.TaskImpl.Worker.InsertWorkerTask;
import SystemLayer.Components.TaskImpl.Worker.StandardQueryWorkerTask;
import SystemLayer.Components.TaskImpl.Worker.WorkerTask;
import SystemLayer.Containers.Configurator.Configurator;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.DataObjectsImpl.DataObject;
import SystemLayer.SystemExceptions.UnknownConfigException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class SystemImpl implements SystemServer {

    private static final String nBands_config = "N_BANDS";

    private final DataContainer context;

    public SystemImpl(DataContainer context ) throws Exception {
        this.context = context;

        //Setup
        Configurator configurator = context.getConfigurator();
        String bands_string = "";
        int bands = 0;
        try {
            bands_string = configurator.getConfig(nBands_config);
            bands = Integer.parseInt(bands_string);
        }catch (Exception e){
            UnknownConfigException.handler( new UnknownConfigException(nBands_config, bands_string));
        }

        //-MultiMaps
        MultimapFactory multimapFactory = new MultimapFactory(context);
        String multimapConfig = configurator.getConfig("MULTIMAP");
        MultiMap[] multiMaps = new MultiMap[bands];
        for ( int i = 0; i<bands; i++ ){
            MultiMap current = multimapFactory.getNewMultiMap(multimapConfig);
            current.setHashBlockPosition(i);
            current.setTotalBlocks(bands);
            multiMaps[i] = current;
        }
        context.setMultiMaps(multiMaps);

        //-Communication
        context.getCommunicationLayer();
    }

    @Override
    public Future insert(DataObject object) throws Exception {
        List<Object> objectList = new ArrayList<>();
        objectList.add(object);
        Message insertMessage = new MessageImpl( Message.types.INSERT_REQUEST, objectList);
        WorkerTask insertWorkerTask = new InsertWorkerTask(insertMessage, context );
        return context.getExecutorService().submit(insertWorkerTask);
    }

    @Override
    public DataObject query(DataObject queryObject) throws Exception {
        List<Object> objectList = new ArrayList<>();
        objectList.add(queryObject);
        Message queryMessage = new MessageImpl( Message.types.QUERY_REQUEST, objectList );
        WorkerTask queryWorkerTask = new StandardQueryWorkerTask(queryMessage, context );
        Future<DataObject> response = context.getExecutorService().submit(queryWorkerTask);
        return response.get();
    }
}
