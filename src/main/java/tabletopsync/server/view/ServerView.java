package tabletopsync.server.view;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ServerView extends JFrame {
    // Text area used to display server logs
    private JTextArea logArea;

    public ServerView() {

        // create a font
        Font font = new Font("SansSerif", Font.PLAIN, 15);

        // try and apply the FlatLaf dark theme for the server window (looks good)
        try { UIManager.setLookAndFeel(new FlatMacDarkLaf());}
        catch (Exception e) {e.printStackTrace();}

        // server window (scroll) and text area with a font
        // frame
        setTitle("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        // text area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(font);
        logArea.setBackground(new Color(24, 28, 34));
        logArea.setForeground(new Color(235, 240, 245));
        logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // scroll pane --> text area
        JScrollPane scrollPane = new JScrollPane(logArea);

        // border
        scrollPane.setBorder(new CompoundBorder(
                new LineBorder(new Color(70, 80, 90), 1, true),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // frame layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // set visible
        setVisible(true);
    }

    /**
     * sets a message to the server window
     * Ensures UI updates occur on the Swing event thread
     */
    public void appendLog(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }
}