package tabletopsync.linker;

import tabletopsync.canvas.view.CanvasView;
import tabletopsync.chatbox.view.ChatView;

import javax.swing.*;
import java.awt.*;

public class TableTopGUI extends JFrame {

    /**
     * This is the primary GUI for the application where we take our modules and combine them.
     * @param chatView the in-game chat GUI
     * @param canvasView the in-game whiteboard area
     */
    public TableTopGUI(ChatView chatView, CanvasView canvasView) {
        this.setTitle("TableTopSync" + " - [INSERT USERNAME HERE]");
        this.setSize(1000, 800);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // Split screen
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                canvasView,              // LEFT: canvas
                chatView.panel        // RIGHT: chat
        );

        splitPane.setDividerLocation(1500);

        add(splitPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

}
