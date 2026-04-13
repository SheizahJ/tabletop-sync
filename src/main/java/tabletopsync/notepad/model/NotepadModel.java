package tabletopsync.notepad.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import tabletopsync.server.model.ServerModel;

import java.io.File;

public class NotepadModel {
    private File currentFile;
    private Note currentNote;
    private final ObjectMapper mapper = ServerModel.mapper;

    public void open(File file) throws Exception {
        if (file == null) {
            loadNewNote();
        } else {
            loadNote(file);
        }
    }

    public void loadNewNote() {
        this.currentFile = null;
        this.currentNote = new Note("Untitled*", "", System.currentTimeMillis());
    }

    public void loadNote(File file) throws Exception {
        this.currentFile = file;
        this.currentNote = mapper.readValue(file, Note.class);
    }

    public void saveCurrentNote(String title, String body) throws Exception {
        if (currentNote == null) {
            return;
        }

        File newFile = new File(ServerModel.NOTES_FOLDER, title + ".json");

        if (currentFile != null && !currentFile.equals(newFile)) {
            currentFile.delete();
        }

        currentNote.setTitle(title);
        currentNote.setBody(body);
        currentNote.updateLastEdited();

        currentFile = newFile;
        mapper.writeValue(currentFile, currentNote);
    }

    public Note getCurrentNote() {
        return currentNote;
    }

    public File getCurrentFile() {
        return currentFile;
    }
}
