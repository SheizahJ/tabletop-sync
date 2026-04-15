package tabletopsync.login;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginView extends JDialog {
    static LoginModel.ColourSwatch selectedSwatch;
    private String submittedUsername;
    private Color submittedColor;

    public LoginView() throws IOException {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        super((Frame) null, "TableTopSync", true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(800, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        selectedSwatch = new LoginModel.ColourSwatch(Color.WHITE);

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
    public void submit(String username) {
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a username before continuing.",
                    "Username Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedSwatch == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a colour before continuing.",
                    "Colour Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        this.submittedUsername = username.trim();
        this.submittedColor = selectedSwatch.getColour();
        this.dispose();
    }

    public String getSubmittedUsername() { return submittedUsername; }
    public Color getSubmittedColor()    { return submittedColor; }
}