package tabletopsync.chatbox.view;

import tabletopsync.chatbox.model.UserMenuModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChatArea extends JPanel {

    private final JTextArea textArea;
    public JTextField textField;
    public JButton sendButton;

    private final UserMenu userMenu;

    public ChatArea() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        JButton showUsersButton = new JButton("Show Users");
        top.add(showUsersButton, BorderLayout.WEST);

        top.setBorder(BorderFactory.createEmptyBorder(5, 2, 15, 2));

        add(top, BorderLayout.NORTH);

        UserMenuModel userMenuModel = new UserMenuModel();
        userMenu = new UserMenu(userMenuModel);
        userMenu.setVisible(false);

        top.add(userMenu, BorderLayout.SOUTH);

        showUsersButton.addActionListener(e -> {
            boolean isVisible = userMenu.isVisible();
            userMenu.setVisible(!isVisible);

            showUsersButton.setText(isVisible ? "Show Users" : "Hide Users");

            top.revalidate();
            top.repaint();
        });

        JPanel center = new JPanel(new BorderLayout());

        textArea = new JTextArea();
        textArea.setMargin(new Insets(5, 5, 5, 5));
        textArea.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        textArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        center.add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(15,0,5,0));

        textField = new JTextField();
        sendButton = new JButton("SEND");

        bottom.add(textField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    public void appendMessage(String msg) {
        textArea.append(msg + "\n");
    }

    public void updateUsers(List<String> users) {
        userMenu.updateUsers(users);
    }
}