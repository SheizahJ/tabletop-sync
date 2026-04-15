package tabletopsync.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 60));

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username: ");
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginBtn = new JButton("Join the Server");
        loginBtn.setFocusPainted(false);
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMargin(new Insets(5, 5, 5, 5));
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            dialogue.submit(username);
        });

        rightPanel.add(loginLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(usernameLabel);
        rightPanel.add(usernameField);
        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(LoginComponents.colorPicker(rightPanel, LoginView.selectedSwatch));
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(loginBtn);
        return rightPanel;
    }

    public static JPanel colorPicker(JPanel panelBackground, LoginModel.ColourSwatch selectedSwatch) {
        JPanel colorPanel = new JPanel(new GridLayout(3, 3));
        colorPanel.setBackground(panelBackground.getBackground());
        colorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        colorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        Color[] colourArray = new Color[]{
                Color.RED, Color.BLUE, Color.GREEN,
                Color.YELLOW, Color.CYAN, Color.MAGENTA,
                Color.PINK, Color.DARK_GRAY
        };

        selectedSwatch.setLabel("SELECTED COLOUR");
        selectedSwatch.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // adding listeners for each color area.
        for (Color color : colourArray) {
            LoginModel.ColourSwatch swatch = getSwatch(color, selectedSwatch);
            colorPanel.add(swatch);
        }

        // adding selectedSwatch last as the 9th cell
        colorPanel.add(selectedSwatch);

        return colorPanel;
    }

    private static LoginModel.ColourSwatch getSwatch(Color color, LoginModel.ColourSwatch selectedSwatch) {
        LoginModel.ColourSwatch swatch = new LoginModel.ColourSwatch(color);
        swatch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginModel.ColourSwatch clicked = (LoginModel.ColourSwatch) e.getSource();
                selectedSwatch.setColour(clicked.getColour());
                selectedSwatch.repaint();
            }
        });
        return swatch;
    }
}