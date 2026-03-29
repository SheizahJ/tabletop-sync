package controller;

import model.ChatModel;
import view.ChatView;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatController {
    private final ChatModel model;
    private final ChatView view;
    private final PrintWriter out;
    public Socket socket;

    public ChatController(ChatModel model, ChatView view, Socket socket, PrintWriter out) {
        this.model = model;
        this.view = view;
        this.socket = socket;
        this.out = out;

        view.setOnClose(this::shutdown);

        view.chatArea.sendButton.addActionListener(_ -> send());
        view.chatArea.textField.addActionListener(_ -> send());
    }

    private void send() {
        String msg = view.chatArea.textField.getText();

        if (!msg.trim().isEmpty()) {
            out.println(msg);
            view.chatArea.textField.setText("");
        }
    }

    public void receive(String msg) {
        model.addMessage(msg);
        SwingUtilities.invokeLater(() -> view.chatArea.appendMessage(msg));
    }

    public void shutdown() {
        try { if (socket != null && !socket.isClosed()) { socket.close(); } }
        catch (Exception ignored) {}
        System.exit(0);
    }
}