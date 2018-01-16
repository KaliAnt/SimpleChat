package chat;
import GUI.chatGUI;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class Server implements User{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    private chatGUI chat;

    //constructor
    public Server() {
      //  super("Instant Messenger"); //window name
        JFrame chatFrame = new JFrame("Instant chat");
        chat = new chatGUI(this);
        chatFrame.setContentPane(chat.Panel);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setVisible(true);
    }

    public void onActionPerformed(String action) {
        sendMessage(action);
    }

    //set up and run the server
    public void startRunning() {
        try {
            server = new ServerSocket(6789, 100); //port, backlog
            while(true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException eofException) {
                    showMessage("\n Server ended the connection! ");
                } finally {
                    //closeCrap();
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //wait for connection, then display connection information
    private void waitForConnection() throws IOException {
        showMessage("Waiting for someone to connect...\n");
        connection = server.accept();
        showMessage("Now connected to" + connection.getInetAddress().getHostName() + "\n");

    }

    //get stream to send and receive data
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream()); //create path to connect to another computer
        output.flush();
        input = new ObjectInputStream(connection.getInputStream()); //create path to receive stuff
        showMessage("\n Streams are now setup! \n");
    }

    //during the chat conversation
    private void whileChatting() throws IOException {
        String message = "You are now connected! ";
        sendMessage(message);
        chat.ableToType(true);
        do {
            //have a conversation
            try {
                message = (String) input.readObject(); //read message
                showMessage("\n" + message);
            } catch(ClassNotFoundException classNotFoundException) {
                showMessage("\nidk wtf that user sent!");
            }
        } while(!message.equals("CLIENT - END")); //when client types END
    }

    //close streams and sockets after you are done chatting
    private void closeCrap(){
        showMessage("\nClosing connections... \n");
        chat.ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //send a message to client
    private void sendMessage(String message) {
        try {
            output.writeObject("SERVER - " +message); //server is username for now;
            output.flush();
            showMessage("\nSERVER - " + message);
        }catch(IOException ioException){
            chat.appendChatWindow("\nERROR: can't send that message!");
        }
    }

    //updates chatWindow
    private void showMessage(final String text) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chat.appendChatWindow(text);
                    }
                }
        );
    }
}
