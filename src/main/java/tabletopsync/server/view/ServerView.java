package tabletopsync.server.view;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ServerView {
    // Text area used to display server logs
    private static JTextArea logArea;

    public ServerView() {

        // try and apply the FlatLaf dark theme for the server window (looks good)
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create main server window
        JFrame frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Text area that displays server messages and activity
        logArea = new JTextArea();
        logArea.setEditable(false);            // Prevent editing
        logArea.setLineWrap(true);             // Wrap long lines
        logArea.setWrapStyleWord(true);        // Wrap at word boundaries
        logArea.setFont(getReadableFont());    // Apply dyslexia-friendly font if available
        logArea.setBackground(new Color(24, 28, 34));
        logArea.setForeground(new Color(235, 240, 245));
        logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Scroll pane allows the log to scroll when messages exceed the window
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Add border styling around the log area
        scrollPane.setBorder(new CompoundBorder(
                new LineBorder(new Color(70, 80, 90), 1, true),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Layout setup for the frame
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);

        // Display the server window
        frame.setVisible(true);
    }

    // Attempts to load OpenDyslexic font if installed, otherwise falls back to SansSerif
    private Font getReadableFont() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // Check if OpenDyslexic exists on the system
        for (String font : ge.getAvailableFontFamilyNames()) {
            if (font.equalsIgnoreCase("OpenDyslexic")) {
                return new Font("OpenDyslexic", Font.PLAIN, 15);
            }
        }

        // Fallback font if OpenDyslexic is not available
        return new Font("SansSerif", Font.PLAIN, 15);
    }

    // Adds a new message to the server log
    public static void log(String message) {

        // Ensures UI updates occur on the Swing event thread
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }
}
