package tabletopsync.chatbox.view;

import javax.swing.*;
import java.awt.*;

/**
 * ----> provides GUI panel container for TableTopGUI
 */
public class ChatView {

    public JPanel panel;
    public ChatArea chatArea;

    /**
     * builds the chatview for a user
     * @param username current client username
     */
    public ChatView(String username) {
        JLabel usernameLabel = new JLabel("USER: " + username);
        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        panel.add(usernameLabel, BorderLayout.NORTH);

        chatArea = new ChatArea();
        panel.add(chatArea, BorderLayout.CENTER);
        chatArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        panel.setSize(800, 600);
        panel.setVisible(true);
    }

    public void setOnClose(Runnable onClose) {}

    public ChatArea getChatArea() {
        return chatArea;
    }
}