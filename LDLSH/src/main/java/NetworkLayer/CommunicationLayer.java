package NetworkLayer;

public interface CommunicationLayer {
    public void broadcast( Message message );
    public void send( Message message );

    public interface Receiver {
        public void run() throws Exception;
    }
}
