package tabletopsync.notepad.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.Consumer;

public class NoteListPanel extends JPanel {
    private JList<File> notesList;
    private JButton newButton;

    public NoteListPanel() {
        notesList = new JList<>();

        notesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof File) {
                    setText(((File) value).getName());
                }

                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(notesList);
        newButton = new JButton("new");

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3,3,3,3);

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

    public File getSelectedNote() {
        return notesList.getSelectedValue();
    }

    public void setListModel(ListModel<File> model) {
        notesList.setModel(model);
    }

    public void addNewButtonListener(ActionListener listener) {
        newButton.addActionListener(listener);
    }

    public void onNoteDoubleClicked(Consumer<File> action) {
        notesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = notesList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        action.accept(notesList.getModel().getElementAt(index));
                    }
                }
            }
        });
    }
}
