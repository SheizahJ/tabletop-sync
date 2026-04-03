package tabletopsync.chatbox.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ChatModel:
 * Holds chat messages and notifies observers
 */
public class ChatModel {
    private final List<String> messages = new ArrayList<>();
    private final List<ChatListener> listeners = new ArrayList<>();

    public void addMessage(String msg) {
        messages.add(msg);
        for (ChatListener l : listeners) l.messageAdded(msg);
    }

    public void addListener(ChatListener listener) { listeners.add(listener); }

    public interface ChatListener {
        void messageAdded(String message);
    }
}