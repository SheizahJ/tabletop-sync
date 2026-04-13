package tabletopsync.notepad.controller;

import tabletopsync.notepad.model.NoteListModel;
import tabletopsync.notepad.model.NotepadModel;
import tabletopsync.notepad.view.NoteListPanel;
import tabletopsync.notepad.view.NotepadPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class NavigationController {
    private final JPanel container;
    private final NoteListModel listModel;
    private final NoteListPanel listView;

    public NavigationController(JPanel container) {
        this.container = container;
        this.listModel = new NoteListModel();
        this.listView = new NoteListPanel();

        new NoteListController(listModel, listView);
        setupListViewListeners();
        showList();
    }

    private void setupListViewListeners() {
        listView.onNoteDoubleClicked(this::showEditor);
        listView.addNewButtonListener(e -> showEditor(null));
    }

    public void showList() {
        listModel.refreshNotes();

        container.removeAll();
        container.add(listView);
        container.revalidate();
        container.repaint();
    }

    public void showEditor(File file) {
        NotepadModel editorModel = new NotepadModel();
        NotepadPanel editorView = new NotepadPanel();

        try {
            editorModel.open(file);

            new NotepadController(editorModel, editorView, this);

            container.removeAll();
            container.add(editorView);
            container.revalidate();
            container.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(container, "Could not open note: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Temporary main for testing
    public static void main(String[] args) {
        JFrame frame = new JFrame("TabletopSync");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel mainContainer = new JPanel(new BorderLayout());
        frame.add(mainContainer);

        new NavigationController(mainContainer);

        frame.setVisible(true);
    }
}
