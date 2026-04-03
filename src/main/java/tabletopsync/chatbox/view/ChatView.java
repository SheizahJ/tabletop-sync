package tabletopsync.chatbox.view;

import javax.swing.*;
import java.awt.*;

/**
 * Chat GUI panel for displaying messages
 */
public class ChatView extends JPanel {

    private final JTextArea chatArea;

    public ChatView(String username) {
        setLayout(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
    }

    public void appendMessage(String message) { chatArea.append(message + "\n"); }
}