package tabletopsync.chatbox.view;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

public class ChatView {

    public JPanel panel;

    private Runnable onClose;

    public ChatArea chatArea;

    public ChatView(String username) {
        try { UIManager.setLookAndFeel(new FlatMacLightLaf()); }
        catch (Exception e) { e.printStackTrace(); }

        panel = new JPanel(new BorderLayout());
        panel.setLayout(new BorderLayout());

        chatArea = new ChatArea();
        panel.add(chatArea, BorderLayout.CENTER);
        chatArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        panel.setSize(800, 600);
        panel.setVisible(true);

    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }
}