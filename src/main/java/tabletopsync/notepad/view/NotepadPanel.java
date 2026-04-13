package tabletopsync.notepad.view;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class NotepadPanel extends JPanel {
    private JTextField titleField;
    private JLabel lastSavedLabel;
    private JTextArea notesArea;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton backButton;

    public NotepadPanel() {
        titleField = new JTextField("Untitled*");
        lastSavedLabel = new JLabel("Last saved: ");
        notesArea = new JTextArea();
        backButton = new JButton("back");
        deleteButton = new JButton("delete");
        saveButton = new JButton("save");

        titleField.setBorder(BorderFactory.createEmptyBorder());
        titleField.setBackground(this.getBackground());
        titleField.setOpaque(false);

        notesArea.setLineWrap(true);
        notesArea.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                SwingUtilities.invokeLater(() -> notesArea.requestFocusInWindow());
            }

            @Override public void ancestorRemoved(AncestorEvent event) {}
            @Override public void ancestorMoved(AncestorEvent event) {}
        });

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3,3,3,3);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        this.add(titleField, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridwidth = 1;
        gbc.gridx = 2;
        gbc.weightx = 0;
        this.add(lastSavedLabel, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        this.add(new JScrollPane(notesArea), gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        this.add(saveButton, gbc);

        gbc.gridx = 1;
        this.add(deleteButton, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 2;
        this.add(backButton, gbc);
    }

    public String getTitleText() {
        return titleField.getText();
    }

    public String getNoteText() {
        return notesArea.getText();
    }

    public void setTitleText(String title) {
        titleField.setText(title);
    }

    public void setNoteText(String text) {
        notesArea.setText(text);
    }

    public void setLastUpdatedLabel(String text) {
        lastSavedLabel.setText("Last edited: " + text);
    }

    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addDeleteListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
