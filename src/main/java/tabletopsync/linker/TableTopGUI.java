package tabletopsync.linker;

import tabletopsync.canvas.controller.CanvasController;
import tabletopsync.canvas.view.CanvasView;
import tabletopsync.chatbox.view.ChatView;
import tabletopsync.notepad.controller.NavigationController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * This is the primary GUI for the application
 * where we take our modules (chat, canvas) and combine them
 */
public class TableTopGUI extends JFrame {

    /**
     * @param chatView   the in-game chat GUI
     * @param canvasView the in-game whiteboard area
     */
    public TableTopGUI(ChatView chatView, CanvasView canvasView) {
        this.setTitle("TableTopSync");
        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setJMenuBar(createMenuBar());

        setLayout(new BorderLayout());

        // get the canvas controller from canvas view
        CanvasController canvasController = (CanvasController) canvasView.getComponent(0);

        // top bar for canvas buttons
        JPanel canvasTopBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        canvasTopBar.setBackground(new Color(0, 0, 0, 0));

        JButton returnOriginButton = new JButton("Return to Origin");

        returnOriginButton.addActionListener(e -> {
            // TODO: create a way to reset the camera (with a button) to 0,0

        });

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

        canvasTopBar.add(changeMapButton);

        // snap to grid feature
        JCheckBox snapToGrid = new JCheckBox("Snap to Grid", true);
        canvasController.model.setSnapToGridState(true);

        snapToGrid.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                canvasController.model.setSnapToGridState(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                canvasController.model.setSnapToGridState(false);
            }
        });

        canvasTopBar.add(snapToGrid);

        // wrapper for canvas area
        JPanel canvasWrapper = new JPanel(new BorderLayout());
        canvasWrapper.add(canvasTopBar, BorderLayout.NORTH);
        canvasWrapper.add(canvasView, BorderLayout.CENTER);

        // notepad panels
        JPanel notepadContainer = new JPanel(new BorderLayout());
        new NavigationController(notepadContainer);
        notepadContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        // split screen
        // Inner split: chat on top, notepad on bottom
        JSplitPane splitPane = getJSplitPane(chatView, notepadContainer, canvasWrapper);
        splitPane.setResizeWeight(0.50); // 50% canvas, 50% right panel apparently

        add(splitPane, BorderLayout.CENTER);
        splitPane.setDividerLocation(1500);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static JSplitPane getJSplitPane(ChatView chatView, JPanel notepadContainer, JPanel canvasWrapper) {
        JSplitPane rightSplitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                chatView.panel,       // TOP: chat
                notepadContainer      // BOTTOM: notepad
        );
        rightSplitPane.setResizeWeight(0.6); // 60% chat, 40% notepad

        // Outer split: canvas on left, right split on right
        return new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                canvasWrapper,        // LEFT: canvas
                rightSplitPane        // RIGHT: chat + notepad stacked
        );
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem ExitMenuItem = new JMenuItem("Exit");
        ExitMenuItem.addActionListener(e -> {
            System.exit(0);
        });
        fileMenu.add(ExitMenuItem);
        menuBar.add(fileMenu);
        return menuBar;
    }
}