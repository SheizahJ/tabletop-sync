package tabletopsync.chatbox.view;

import javax.swing.*;
import java.awt.*;

public class ChatView {

    public JPanel panel;

    public ChatArea chatArea;

    public ChatView(String username) {
        panel = new JPanel(new BorderLayout());
        panel.setLayout(new BorderLayout());

        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

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