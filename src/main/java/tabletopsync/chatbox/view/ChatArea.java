package tabletopsync.chatbox.view;

import com.formdev.flatlaf.FlatLightLaf;
import tabletopsync.chatbox.model.UserMenuModel;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main chat area GUI
 * - Displays messages
 * - Shows system messages
 * - Allows user input
 * - Toggle user list
 */
public class ChatArea extends JPanel {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    private final JPanel chatPanel;
    private final JTextArea systemTextArea;
    private final Color selectedColour;
    private final UserMenu userMenu;
    public JTextField textField;
    public JButton sendButton;

    public ChatArea(Color selectedColour) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        this.selectedColour = selectedColour;
        setLayout(new BorderLayout());

        // TOP
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JButton showUsersButton = new JButton("Show Users");
        top.add(showUsersButton, BorderLayout.WEST);
        top.setBorder(BorderFactory.createEmptyBorder(5, 2, 15, 2));
        add(top, BorderLayout.NORTH);

        UserMenuModel userMenuModel = new UserMenuModel();
        userMenu = new UserMenu(userMenuModel);
        userMenu.setVisible(false);
        top.add(userMenu, BorderLayout.SOUTH);

        showUsersButton.addActionListener(_ -> {
            boolean visible = userMenu.isVisible();
            userMenu.setVisible(!visible);
            showUsersButton.setText(visible ? "Show Users" : "Hide Users");
            top.revalidate();
            top.repaint();
        });

        // CENTER
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setOpaque(false);

        JScrollPane chatScroll = new JScrollPane(chatPanel);
        chatScroll.setOpaque(false);
        chatScroll.getViewport().setOpaque(false);

        systemTextArea = new JTextArea();
        systemTextArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Systems"),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );
        systemTextArea.setMargin(new Insets(5, 5, 5, 5));
        systemTextArea.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        systemTextArea.setForeground(Color.GRAY);
        systemTextArea.setLineWrap(true);
        systemTextArea.setEditable(false);

        JScrollPane systemScroll = new JScrollPane(systemTextArea);
        systemScroll.setOpaque(false);
        systemScroll.getViewport().setOpaque(false);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                systemScroll,
                chatScroll        // no longer double-wrapped
        );
        splitPane.setOpaque(false);
        splitPane.setDividerLocation(150);
        splitPane.setResizeWeight(0.3);

        center.add(splitPane, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // BOTTOM
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        textField = new JTextField();
        sendButton = new JButton("SEND");
        bottom.add(textField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Prints the message onto the chat or system area
     *
     * @param username your username
     * @param message  your message
     * @param color    the assigned colour to the user
     */
    public void appendMessage(String username, String message, Color color) {

        if (username.equals("SYSTEM")) {
            systemTextArea.append(message + "\n");
            systemTextArea.setCaretPosition(systemTextArea.getDocument().getLength());
            return;
        }

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setOpaque(false);

        JComponent colorBox = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(color);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        colorBox.setPreferredSize(new Dimension(12, 12));
        colorBox.setMinimumSize(new Dimension(12, 12));
        colorBox.setMaximumSize(new Dimension(12, 12));

        String time = formatter.format(Instant.now());

        JLabel label = new JLabel(
                "<html>" + time + "<br> " + "<b>" + username + ":</b> " + message + "</html>"
        );

        row.add(colorBox);
        row.add(Box.createHorizontalStrut(5));
        row.add(label);

        chatPanel.add(row);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    /**
     * Updates the displayed user list
     *
     * @param users current usernames
     */
    public void updateUsers(List<String> users) {
        userMenu.updateUsers(users);
    }

//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        // rectangle
//        g.setColor(selectedColour);
//        g.fillRect(0, 0, getWidth(), getHeight());
//
//        // border
//        g.setColor(Color.BLACK);
//        g.drawRect(0, 0, getWidth(), getHeight());
//
//    }
}