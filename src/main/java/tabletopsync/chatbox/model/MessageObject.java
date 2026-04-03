package tabletopsync.chatbox.model;

import java.sql.Time;

/**
 * Serializable chat or system message
 */
public class MessageObject {

    private String username;
    private String message;
    private Time timestamp;
    private String type; // CHAT or SYSTEM

    public MessageObject() {} // default for JSON deserialization

    public MessageObject(String username, String message, Time timestamp, String type) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
    }

    // getters and setters
    public String getUsername() { return username; }
    public String getMessage() { return message; }
    public Time getTimestamp() { return timestamp; }
    public String getType() { return type; }
}