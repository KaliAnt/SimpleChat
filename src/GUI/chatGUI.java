package GUI;

import chat.User;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class chatGUI {
    public JPanel Panel;
    private JButton Send;
    private JTextField userText;
    private JTextArea chatWindow;
    private User user;

    public chatGUI(User user) {

        this.user=user;
        userText.setEditable(false);
//        chatWindow.add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        userText.setVisible(true);
       // Send.setFocusable(false);
        userText.addActionListener(
            new ActionListener() {
              @Override
                 public void actionPerformed(ActionEvent e) {
                 //sendMessage(e.getActionCommand());
                  user.onActionPerformed(e.getActionCommand());
                  userText.setText("");

            }
        }

        );

        Send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.onActionPerformed(userText.getText());
                userText.setText("");

            }
        });
    }

    private void createUIComponents() {

        // TODO: place custom component creation code here
    }

    public void appendChatWindow(String message){
        chatWindow.append(message);
    }

    public void ableToType(final boolean tof){
        userText.setEditable(tof);
        Send.setEnabled(tof);
    }


}
