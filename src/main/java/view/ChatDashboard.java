package view;

import controller.ChatService;

import javax.swing.*;
import java.awt.*;

public class ChatDashboard extends JPanel {

    private DefaultListModel<String> model;
    private JList<String> userList;

    public ChatDashboard() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 400)); // fix size

        JLabel title = new JLabel("Dashboard");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // ✅ initialize FIELD, not local
        model = new DefaultListModel<>();
        userList = new JList<>(model);

        // ✅ wrap in scroll pane
        add(new JScrollPane(userList), BorderLayout.CENTER);

        refresh(); // initial load
    }

    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            model.clear();

            for (String username : ChatService.clients.keySet()) {
                model.addElement(username);
            }
        });
    }
}