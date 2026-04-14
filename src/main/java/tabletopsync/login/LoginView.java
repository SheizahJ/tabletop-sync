package tabletopsync.login;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginView extends JFrame {

    public LoginView() throws IOException {
        this.setTitle("TableTopSync");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Connect to Server");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // ===== Center Wrapper =====
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        centerWrapper.add(separator);

        centerWrapper.add(Box.createVerticalStrut(20));

        JPanel loginPanel = getJPanel();

        // center it nicely
        centerWrapper.add(Box.createVerticalGlue());
        centerWrapper.add(loginPanel);
        centerWrapper.add(Box.createVerticalGlue());

        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        this.setContentPane(mainPanel);
        this.setVisible(true);
    }

    private static JPanel getJPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
        loginPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField usernameField = new JTextField();

        JButton loginButton = new JButton("Join");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(loginButton);
        return loginPanel;
    }

}