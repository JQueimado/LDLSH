package SystemLayer.Components.NetworkLayer.NettyCommunicationLayerPackage;

public class NettyServerException extends Exception {

    public static void handler( NettyServerException e ){
        System.err.println(e.getMessage());
        e.printStackTrace();
    }

    public NettyServerException(String message){
        super(message);
    }

}
