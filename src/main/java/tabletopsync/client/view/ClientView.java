package tabletopsync.client.view;

import tabletopsync.canvas.controller.CanvasController;
import tabletopsync.canvas.view.CanvasView;
import tabletopsync.chatbox.view.ChatView;
import tabletopsync.notepad.controller.NavigationController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;

/**
 * This is the primary GUI for the application
 * where we take our modules (chat, canvas) and combine them
 */
public class ClientView extends JFrame {

    /**
     * @param chatView the in-game chat GUI
     * @param canvasView the in-game whiteboard area
     */
    public ClientView(ChatView chatView, CanvasView canvasView) {
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

        JButton addSpriteButton = getSpriteButton(canvasController);
        canvasTopBar.add(addSpriteButton);

        JButton changeMapButton = getMapButton(canvasController);
        canvasTopBar.add(changeMapButton);

        JCheckBox snapToGrid = getSnapButton(canvasController);
        canvasTopBar.add(snapToGrid);

        // wrapper for canvas area
        JPanel canvasWrapper = new JPanel(new BorderLayout());
        canvasWrapper.add(canvasTopBar, BorderLayout.NORTH);
        canvasWrapper.add(canvasView, BorderLayout.CENTER);

        // notepad panels
        JPanel notepadContainer = new JPanel(new BorderLayout());
        new NavigationController(notepadContainer);

        JPanel rightPanel = new JPanel(new BorderLayout());

        rightPanel.add(chatView.panel, BorderLayout.CENTER);
        rightPanel.add(notepadContainer, BorderLayout.SOUTH);

        // split screen
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                canvasWrapper,    // LEFT: canvas area with button
                rightPanel    // RIGHT: chat
        );
        splitPane.setDividerLocation(1500);
        add(splitPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton getMapButton(CanvasController canvasController) {
        JButton changeMapButton = new JButton("Change Map");

        changeMapButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose map image");

            FileNameExtensionFilter filter =
                    new FileNameExtensionFilter("Image Files (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg");

            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                canvasController.setMapFromFile(selectedFile);
            }
        });
        return changeMapButton;
    }

    private JButton getSpriteButton(CanvasController canvasController) {
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
        return addSpriteButton;
    }

    private JCheckBox getSnapButton(CanvasController canvasController) {
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
        return snapToGrid;
    }
}