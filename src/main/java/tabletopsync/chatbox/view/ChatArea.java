package tabletopsync.chatbox.view;

import javax.swing.*;
import java.awt.*;

public class ChatArea extends JPanel {

    private final JTextArea textArea;
    public JTextField textField;
    public JButton sendButton;

    public ChatArea() {
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setMargin(new Insets(5, 5, 5, 5));
        textArea.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        textArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        textField = new JTextField();
        sendButton = new JButton("SEND");

        bottom.add(textField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    public void appendMessage(String msg) {
        textArea.append(msg + "\n");
    }
}