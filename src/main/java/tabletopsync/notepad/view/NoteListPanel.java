package tabletopsync.notepad.view;

import tabletopsync.server.model.ServerModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class NoteListPanel extends JPanel {
    private JList<String> notesList;
    private JButton newButton;

    public NoteListPanel() {
        File[] files = ServerModel.NOTES_FOLDER.listFiles();
        String[] notes = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            notes[i] = files[i].getName();
        }

        notesList = new JList<>(notes);
        JScrollPane scrollPane = new JScrollPane(notesList);
        newButton = new JButton("new");

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        this.add(scrollPane,gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        this.add(newButton,gbc);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.add(new NoteListPanel());
        frame.setVisible(true);
    }
}
