package tabletopsync.login;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginView extends JFrame {

    public LoginView() throws IOException {
        this.setTitle("TableTopSync");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);


        JPanel panel = new JPanel(new BorderLayout());

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


        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField username = new JTextField();
        username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));


        JButton loginBtn = new JButton("Sign In");
        loginBtn.setFocusPainted(false);

        rightPanel.add(loginLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(username);
        rightPanel.add(Box.createVerticalStrut(10));
        // add colours here

        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(loginBtn);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);

        panel.add(splitPane, BorderLayout.CENTER);
        this.add(panel);
        this.setVisible(true);
    }
}