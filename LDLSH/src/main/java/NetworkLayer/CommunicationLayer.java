package NetworkLayer;

public interface CommunicationLayer {
    public interface Transmitter {
        public void broadcast( Message message );
        public void send( Message message );
    }
    public interface Receiver {
        public void run();
    }
}
