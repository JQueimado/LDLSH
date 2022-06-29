package SystemLayer.Components.MultiMapImpl;

import NetworkLayer.Message;
import NetworkLayer.MessageImpl;
import SystemLayer.Containers.DataContainer;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl;
import SystemLayer.Data.ErasureCodesImpl.ErasureCodesImpl.ErasureBlock;
import SystemLayer.Data.LSHHashImpl.LSHHash;
import SystemLayer.Data.LSHHashImpl.LSHHashImpl;
import SystemLayer.Data.UniqueIndentifierImpl.UniqueIdentifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RemoteMultimap extends MultiMapImpl{

    //Remote
    private DataContainer appContext;
    private String host = "";
    private int port = 0;

    public RemoteMultimap( DataContainer appContext ){
        super(appContext);
        this.appContext = appContext;
    }

    @Override
    public void insert(LSHHash lshHash, UniqueIdentifier uniqueIdentifier, ErasureBlock erasureBlock) {
        List<Serializable> messageBody = new ArrayList<>();
        messageBody.add(lshHash);
        messageBody.add(uniqueIdentifier);
        messageBody.add(erasureBlock);
        Message insertMessage = new MessageImpl(Message.types.INSERT_MESSAGE, messageBody);
        Message response = appContext.getCommunicationLayer().send(insertMessage, host, port);
    }

    @Override
    public ErasureBlock complete(LSHHash lshHash, UniqueIdentifier uniqueIdentifier) {
        return null;
    }

    @Override
    public MultiMapValue[] query(LSHHashImpl.LSHHashBlock lshHash) {
        return new MultiMapValue[0];
    }

}
