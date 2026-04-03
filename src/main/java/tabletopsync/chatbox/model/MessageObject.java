package tabletopsync.chatbox.model;

import java.sql.Time;

/**
 * Represents a single chat or system message
 */
public class MessageObject {

    private String username;
    private String message;
    private Time timestamp;
    private String type;

    public MessageObject() {
        // Required for Jackson API
    }

    public MessageObject(String username, String message, Time timestamp, String type) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Time getTimestamp() { return timestamp; }
    public void setTimestamp(Time timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "username=" + username + ", message=" + message + ", timestamp=" + timestamp + ", type=" + type;
    }
}