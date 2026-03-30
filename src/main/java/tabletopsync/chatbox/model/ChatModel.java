package tabletopsync.chatbox.model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ChatModel {
    private final List<String> messages = new ArrayList<>();

    public void addMessage(String msg) {
        messages.add(msg);
    }

    DefaultListModel<String> userList = new DefaultListModel<>();

    public List<String> getMessages() {
        return messages;
    }
}