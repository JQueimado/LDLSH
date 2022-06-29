package NetworkLayer;

public interface CommunicationLayer {
    public Message send( Message message, String hostname, int port );

    public interface Receiver {
        public void run() throws Exception;
    }
}
