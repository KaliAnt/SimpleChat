package chat;
import javax.swing.JFrame;

public class ClientTest {

    public static void main(String[] args) {
        Client client;
        client = new Client("127.0.0.1");
        client.startRunning();
    }

}
