package tabletopsync.login;

import javax.swing.*;
import java.awt.*;

public class LoginComponents {

    public static JPanel leftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.BLACK);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("TableTopSync");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Welcome to");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(180, 180, 180));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(subtitle);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(title);
        leftPanel.add(Box.createVerticalGlue());
        return leftPanel;
    }

    public static JPanel rightPanel(LoginView dialogue) {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username: ");

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        // Build the button here so it can reference usernameField and dialogue directly
        JButton loginBtn = new JButton("Join the Server");
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(e -> {
            // Read the field NOW, at click time, not at construction time
            String username = usernameField.getText();
            dialogue.submit(username, null); // null color until colorPicker is wired up
        });

        rightPanel.add(loginLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(usernameLabel);
        rightPanel.add(usernameField);
        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(colorPicker());
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(loginBtn);
        return rightPanel;
    }

    public static JButton colorPicker() {
        return new JButton("Pick a Colour");
    }
}