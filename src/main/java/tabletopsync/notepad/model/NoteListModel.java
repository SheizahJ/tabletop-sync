package tabletopsync.notepad.model;

import tabletopsync.server.model.ServerModel;

import javax.swing.*;
import java.io.File;

public class NoteListModel {
    private DefaultListModel<File> listModel = new DefaultListModel<>();

    public void refreshNotes() {
        listModel.clear();

        if (!ServerModel.NOTES_FOLDER.exists()) {
            ServerModel.NOTES_FOLDER.mkdirs();
        }

        File[] files = ServerModel.NOTES_FOLDER.listFiles();

        if (files != null) {
            for (File file : files) {
                listModel.addElement(file);
            }
        }
    }

    public DefaultListModel<File> getListModel() {
        return listModel;
    }
}
