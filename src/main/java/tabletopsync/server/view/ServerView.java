package tabletopsync.server.view;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Simple server GUI that displays logs
 */
public class ServerView extends JFrame {
    private final JTextArea logArea;

    public ServerView() {
        Font font = new Font("SansSerif", Font.PLAIN, 15);

        try { UIManager.setLookAndFeel(new FlatMacDarkLaf()); }
        catch (Exception e) { e.printStackTrace(); }

        setTitle("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(font);
        logArea.setBackground(new Color(24, 28, 34));
        logArea.setForeground(new Color(235, 240, 245));
        logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(new CompoundBorder(
                new LineBorder(new Color(70, 80, 90), 1, true),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public void appendLog(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }
}