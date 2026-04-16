package tabletopsync.chatbox.view;

import tabletopsync.server.model.ServerModel;

import javax.swing.*;
import java.awt.*;

/**
 * ChatView wraps the main chat area with username label
 * and provides GUI container for TableTopGUI
 */
public class ChatView {

    public JPanel panel;
    public ChatArea chatArea;

    /**
     * Constructs the chat view for a specific username
     *
     * @param username       current client username
     * @param colorSelection
     */
    public ChatView(String username, Color colorSelection) {

        JLabel usernameLabel = new JLabel(" USER: " + username);
        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        usernameLabel.setForeground(colorSelection);
        panel.add(usernameLabel, BorderLayout.NORTH);

        chatArea = new ChatArea(colorSelection);
        panel.add(chatArea, BorderLayout.CENTER);
        chatArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        panel.setSize(800, 600);
        panel.setVisible(true);
    }


    /**
     * Placeholder for window close handling
     * @param onClose callback
     */
    public void setOnClose(Runnable onClose) {}

    public ChatArea getChatArea() {
        return chatArea;
    }
}