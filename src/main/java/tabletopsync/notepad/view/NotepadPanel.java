package tabletopsync.notepad.view;

import javax.swing.*;
import java.awt.*;

public class NotepadPanel extends JPanel {
    private JTextArea notesArea;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton backButton;

    public NotepadPanel() {
        notesArea = new JTextArea();
        backButton = new JButton("back");
        deleteButton = new JButton("delete");
        saveButton = new JButton("save");

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        this.add(notesArea, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        this.add(saveButton, gbc);

        gbc.gridx = 1;
        this.add(deleteButton, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 2;
        this.add(backButton, gbc);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.add(new NotepadPanel());
        frame.setVisible(true);
    }
}
