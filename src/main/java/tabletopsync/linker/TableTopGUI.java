package tabletopsync.linker;

import javax.swing.*;
import java.awt.*;

/**
 * TableTopGUI links multiple modules (chat + canvas) into a single window
 * - Acts as the main client GUI frame
 * - Embeds ChatView and CanvasView side by side
 */
public class TableTopGUI extends JFrame {

    /**
     * Constructs the main GUI
     * @param chatPanel the chat module panel
     * @param canvasPanel the canvas module panel
     */
    public TableTopGUI(JPanel chatPanel, JPanel canvasPanel) {
        setTitle("Tabletop Sync");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Add canvas panel to center
        add(canvasPanel, BorderLayout.CENTER);

        // Add chat panel to the right side
        add(chatPanel, BorderLayout.EAST);

        // Optional: set preferred width for chat panel
        chatPanel.setPreferredSize(new Dimension(350, 800));

        setVisible(true);
    }
}