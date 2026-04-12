package tabletopsync.notepad.model;

import java.time.Instant;

public class NoteObject {
    String title;
    String body;
    Instant lastEdited;

    public NoteObject() {} // Required for Jackson API

    public NoteObject(String title, String body, Instant lastEdited) {
        this.title = title;
        this.body = body;
        this.lastEdited = lastEdited;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Instant getLastEdited() {
        return lastEdited;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setLastEdited() {
        lastEdited = Instant.now();
    }
}
