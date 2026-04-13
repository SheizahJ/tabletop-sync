package tabletopsync.notepad.model;

public class Note {
    String title;
    String body;
    long lastSaved;

    public Note() {} // Required for Jackson API

    public Note(String title, String body, long lastSaved) {
        this.title = title;
        this.body = body;
        this.lastSaved = lastSaved;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public long getLastSaved() {
        return lastSaved;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setLastSaved(long lastSaved) {
        this.lastSaved = lastSaved;
    }

    public void updateLastEdited() {
        lastSaved = System.currentTimeMillis();
    }
}
