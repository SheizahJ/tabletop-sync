package tabletopsync.chatbox.view;

import tabletopsync.chatbox.model.UserMenuModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main chat area GUI
 * - Displays messages
 * - Shows system messages
 * - Allows user input
 * - Toggle user list
 */
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
            boolean visible = userMenu.isVisible();
            userMenu.setVisible(!visible);
            showUsersButton.setText(visible ? "Show Users" : "Hide Users");
            top.revalidate();
            top.repaint();
        });

        JPanel center = new JPanel(new BorderLayout());

<<<<<<< Updated upstream
        textArea = new JTextArea();
        textArea.setMargin(new Insets(5, 5, 5, 5));
        textArea.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        textArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        center.add(new JScrollPane(textArea), BorderLayout.CENTER);
=======
        chatTextArea = new JTextArea();
        chatTextArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Communications"),
                        BorderFactory.createEmptyBorder(15,15,15,15)
                )
        );
        chatTextArea.setMargin(new Insets(5, 5, 5, 5));
        chatTextArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        chatTextArea.setLineWrap(true);
        chatTextArea.setEditable(false);

        systemTextArea = new JTextArea();
        systemTextArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Systems"),
                        BorderFactory.createEmptyBorder(10,10,10,10)
                )
        );
        systemTextArea.setMargin(new Insets(5, 5, 5, 5));
        systemTextArea.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        systemTextArea.setForeground(Color.GRAY);
        systemTextArea.setLineWrap(true);
        systemTextArea.setEditable(false);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(systemTextArea),
                new JScrollPane(chatTextArea)
        );
        splitPane.setDividerLocation(150);
        splitPane.setResizeWeight(0.3);

        center.add(splitPane, BorderLayout.CENTER);
>>>>>>> Stashed changes
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(15,0,5,0));
        textField = new JTextField();
        sendButton = new JButton("SEND");
        bottom.add(textField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Prints the message onto the chat or system area
     * @param msg the message to display
     */
    public void appendMessage(String msg) {
<<<<<<< Updated upstream
        textArea.append(msg + "\n");
=======
        // Ignore user list messages
        if (msg.startsWith("USER_LIST")) return;

        // System messages
        if (msg.contains("joined") || msg.contains("left") || msg.contains("disconnected")) {
            systemTextArea.append(msg + "\n");
            systemTextArea.setCaretPosition(systemTextArea.getDocument().getLength());
        } else {
            chatTextArea.append(msg + "\n");
            chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
        }
>>>>>>> Stashed changes
    }

    /**
     * Updates the displayed user list
     * @param users current usernames
     */
    public void updateUsers(List<String> users) {
        userMenu.updateUsers(users);
    }
}