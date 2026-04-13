package tabletopsync.notepad.controller;

import tabletopsync.notepad.model.NoteListModel;
import tabletopsync.notepad.view.NoteListPanel;

public class NoteListController {
    private NoteListModel model;
    private NoteListPanel view;

    public NoteListController(NoteListModel model, NoteListPanel view) {
        this.model = model;
        this.view = view;

        this.view.setListModel(model.getListModel());
        model.refreshNotes();
    }
}
