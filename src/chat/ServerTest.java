package chat;

import javax.swing.JFrame;

public class ServerTest {
    public static void main(String[] args) {
        Server some = new Server();
      //  some.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        some.startRunning();
    }
}
