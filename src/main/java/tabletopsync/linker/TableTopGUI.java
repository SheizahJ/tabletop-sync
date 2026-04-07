package tabletopsync.linker;

import tabletopsync.canvas.controller.CanvasController;
import tabletopsync.canvas.view.CanvasView;
import tabletopsync.chatbox.view.ChatView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;

/**
 * This is the primary GUI for the application
 * where we take our modules (chat, canvas) and combine them
 */
public class TableTopGUI extends JFrame {

    /**
     * @param chatView the in-game chat GUI
     * @param canvasView the in-game whiteboard area
     */
    public TableTopGUI(ChatView chatView, CanvasView canvasView) {
        this.setTitle("TableTopSync");
        this.setSize(1000, 800);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // get the canvas controller from canvas view
        CanvasController canvasController = (CanvasController) canvasView.getComponent(0);

        // top bar for canvas buttons
        JPanel canvasTopBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        canvasTopBar.setBackground(Color.WHITE);

        JButton addSpriteButton = new JButton("Add Sprite");

        addSpriteButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a sprite");

            // allow png / jpg / jpeg only
            FileNameExtensionFilter imageFilter =
                    new FileNameExtensionFilter("Image Files (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg");
            fileChooser.setFileFilter(imageFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                canvasController.addSpriteFromFile(selectedFile);
            }
        });

        canvasTopBar.add(addSpriteButton);

        // snap to grid feature
        JCheckBox snapToGrid = new JCheckBox("Snap to Grid", true);
        canvasController.model.setSnapToGridState(true);

        snapToGrid.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                canvasController.model.setSnapToGridState(true);
            }
            else if (e.getStateChange() == ItemEvent.DESELECTED) {
                canvasController.model.setSnapToGridState(false);
            }
        });

        canvasTopBar.add(snapToGrid);

        // wrapper for canvas area
        JPanel canvasWrapper = new JPanel(new BorderLayout());
        canvasWrapper.add(canvasTopBar, BorderLayout.NORTH);
        canvasWrapper.add(canvasView, BorderLayout.CENTER);

        // split screen
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                canvasWrapper,    // LEFT: canvas area with button
                chatView.panel    // RIGHT: chat
        );
        splitPane.setDividerLocation(1500);
        add(splitPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}