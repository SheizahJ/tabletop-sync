package tabletopsync.login;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginView extends JDialog {
    private String submittedUsername;
    private String submittedColor;

    public LoginView() throws IOException {
        super((Frame) null, "TableTopSync", true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(800, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                LoginComponents.leftPanel(),
                LoginComponents.rightPanel(this)
        );
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);
        panel.add(splitPane, BorderLayout.CENTER);
        this.add(panel);
    }

    // Called by your login button in LoginComponents
    public void submit(String username, String color) {
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a username before continuing.",
                    "Username Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.submittedUsername = username.trim();
        this.submittedColor = color;
        this.dispose();
    }

    public String getSubmittedUsername() { return submittedUsername; }
    public String getSubmittedColor()    { return submittedColor; }
}