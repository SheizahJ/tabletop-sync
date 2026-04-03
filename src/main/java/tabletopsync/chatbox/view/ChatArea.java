package tabletopsync.chatbox.view;

import javax.swing.*;
import java.awt.*;

/**
 * Chat text area for displaying messages
 */
public class ChatArea extends JTextArea {

    public ChatArea() {
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    public void appendMessage(String message) { append(message + "\n"); }
}