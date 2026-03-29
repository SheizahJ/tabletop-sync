package view;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import controller.ChatService;

import javax.swing.*;
import java.awt.*;

public class ChatView {

    JFrame frame;
    private Runnable onClose;

    public ChatArea chatArea;
    public ChatDashboard dashboard;

    public ChatView(String username) {
        try { UIManager.setLookAndFeel(new FlatMacLightLaf()); }
        catch (Exception e) { e.printStackTrace(); }

        frame = new JFrame(username + "'s Chat");
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        chatArea = new ChatArea();
        dashboard = new ChatDashboard();
        ChatService.setDashboard(dashboard);

        chatArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                chatArea,
                dashboard
        );

        splitPane.setDividerLocation(550);
        splitPane.setResizeWeight(0.75);
        splitPane.setEnabled(false);

        frame.add(splitPane, BorderLayout.CENTER);

        frame.setAlwaysOnTop(true);
        frame.setSize(800, 600);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (onClose != null) {
                    onClose.run();
                }
                frame.dispose();
            }
        });
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }
}