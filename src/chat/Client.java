package chat;
import GUI.chatGUI;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Client implements User{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message;
    private String serverIP;
    private Socket connection;

    private chatGUI chat;

    public Client(String host){

        serverIP = host;
        JFrame chatFrame = new JFrame("Instant chat");
        chat = new chatGUI(this);
        chatFrame.setContentPane(chat.Panel);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setVisible(true);
    }

    public void onActionPerformed(String action){
        sendMessage(action);
    }

    //connect through server
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }catch(EOFException eofException) {
            showMessage("\nClient terminated connection");

        }catch (IOException ioException) {
            ioException.printStackTrace();
        }finally {
            closeCrap();
        }
    }

    //connect to server
    private void connectToServer() throws IOException{
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP),6789);
        showMessage("Connected to:" + connection.getInetAddress().getHostName());
    }

    //set up streams to send and receive messages
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nEstablished streams\n");
    }

    //while chatting with server
    private void whileChatting() throws IOException{
        chat.ableToType(true);
        do{
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            }catch (ClassNotFoundException classNotFoundException) {
                showMessage("\nUnknown object type");
            }
        }while(!message.equals("SERVER - END"));
    }

    //close the streams and sockets
    private void closeCrap(){
        showMessage("\nClosing streams...");
        chat.ableToType(true);
        try {
            output.close();
            input.close();
            connection.close();
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //send messages to server
    private void sendMessage(String message) {
        try{
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        }catch (IOException ioException){
            showMessage("\nError at sending message!");
        }
    }

    //change/update chatWindow
    private void showMessage(final String m) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chat.appendChatWindow(m); //append
                    }
                }
        );
    }

}
