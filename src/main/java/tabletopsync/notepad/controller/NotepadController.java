package tabletopsync.notepad.controller;

import tabletopsync.notepad.model.NotepadModel;
import tabletopsync.notepad.view.NotepadPanel;

import javax.swing.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class NotepadController {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    private NotepadModel model;
    private NotepadPanel view;

    public NotepadController(NotepadModel model, NotepadPanel view, NavigationController nav) {
        this.model = model;
        this.view = view;

        if (model.getCurrentNote() != null) {
            view.setTitleText(model.getCurrentNote().getTitle());
            view.setNoteText(model.getCurrentNote().getBody());
        } else {
            view.setTitleText("Untitled*");
            view.setNoteText("");
        }

        view.addSaveListener(e -> {
            try {
                String title = view.getTitleText();
                String body = view.getNoteText();
                model.saveCurrentNote(title, body);

                // Update title view (in case it was "Untitled" and is now named)
                view.setTitleText(model.getCurrentNote().getTitle());

                System.out.println("Note saved successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Save Failed: " + ex.getMessage());
            }
        });

        view.addDeleteListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view,
                    "Are you sure you want to delete this note?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (model.getCurrentFile() != null && model.getCurrentFile().exists()) {
                    if (model.getCurrentFile().delete()) {
                        nav.showList();
                    }
                }
            }
        });

        view.addBackListener(e -> {
            nav.showList();
        });

        updateViewFromModel();
    }

    private void updateViewFromModel() {
        view.setTitleText(model.getCurrentNote().getTitle());
        view.setNoteText(model.getCurrentNote().getBody());

        long timestamp = model.getCurrentNote().getLastSaved();

        if (timestamp > 0) {
            String dateString = formatter.format(Instant.ofEpochMilli(timestamp));
            view.setLastUpdatedLabel(dateString);
        }
    }
}
