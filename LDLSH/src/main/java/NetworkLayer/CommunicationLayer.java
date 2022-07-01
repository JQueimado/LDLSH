package NetworkLayer;

import io.netty.util.concurrent.Promise;

public interface CommunicationLayer {
    public Promise<Message> send(Message message, String hostname, int port ) throws Exception ;

    public interface Receiver {
        public void run() throws Exception;
    }
}
