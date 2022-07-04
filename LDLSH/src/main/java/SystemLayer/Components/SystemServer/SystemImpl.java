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

    private enum NodeTypes { MULTIMAP_SERVER, CLIENT_WORKER }

    private static final String nBands_config = "N_BANDS";
    private static final String nodeType_config = "NODE_TYPE";
    private static final String multiMapPosition_config = "MULTIMAP_POSITION";

    private final DataContainer context;

    public SystemImpl(DataContainer context ) throws Exception {
        this.context = context;

        Configurator configurator = context.getConfigurator();

        String nodeType_value = "";
        NodeTypes nodeType;
        try{
            nodeType_value = configurator.getConfig(nodeType_config);
            nodeType = NodeTypes.valueOf( nodeType_value );
        }catch (Exception e){
            UnknownConfigException.handler( new UnknownConfigException(nodeType_config, nodeType_value) );
            return;
        }

        switch (nodeType){
            case MULTIMAP_SERVER -> {
                //-MultiMaps
                MultimapFactory multimapFactory = new MultimapFactory(context); //Create factory
                String multimapConfig = configurator.getConfig("MULTIMAP"); //Get multimap Config

                //Get Bands
                String bands_string = "";
                int bands = 0;
                try {
                    bands_string = configurator.getConfig(nBands_config);
                    bands = Integer.parseInt(bands_string);
                }catch (Exception e){
                    UnknownConfigException.handler( new UnknownConfigException(nBands_config, bands_string));
                }

                //Get Map position
                String multiMapPosition_value = "";
                int multiMapPosition;
                try {
                    multiMapPosition_value = configurator.getConfig(multiMapPosition_config);
                    multiMapPosition = Integer.parseInt(multiMapPosition_value);
                }catch (Exception e){
                    UnknownConfigException.handler(
                            new UnknownConfigException(multiMapPosition_config, multiMapPosition_value)
                    );
                    return;
                }

                MultiMap[] multiMaps = new MultiMap[1]; //Set array to size 1

                MultiMap current = multimapFactory.getNewMultiMap(multimapConfig); //get new Multimap
                current.setHashBlockPosition(multiMapPosition);
                current.setTotalBlocks(bands);

                multiMaps[0] = current;
                context.setMultiMaps(multiMaps);
            }

            case CLIENT_WORKER -> {
                //-MultiMaps
                MultimapFactory multimapFactory = new MultimapFactory(context); //Create factory
                String multimapConfig = configurator.getConfig("MULTIMAP"); //Get multimap Config

                //Get Bands
                String bands_string = "";
                int bands = 0;
                try {
                    bands_string = configurator.getConfig(nBands_config);
                    bands = Integer.parseInt(bands_string);
                }catch (Exception e){
                    UnknownConfigException.handler( new UnknownConfigException(nBands_config, bands_string));
                }

                MultiMap[] multiMaps = new MultiMap[bands];
                for ( int i = 0; i<bands; i++ ){
                    MultiMap current = multimapFactory.getNewMultiMap(multimapConfig);
                    current.setHashBlockPosition(i);
                    current.setTotalBlocks(bands);
                    multiMaps[i] = current;
                }
                context.setMultiMaps(multiMaps);
            }
        }

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
