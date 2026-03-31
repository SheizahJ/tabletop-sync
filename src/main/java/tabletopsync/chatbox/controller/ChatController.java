package tabletopsync.chatbox.controller;

import tabletopsync.chatbox.model.ChatModel;
import tabletopsync.chatbox.view.ChatView;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ChatController {
    private final ChatModel model;
    private final ChatView view;
    private final PrintWriter out;
    public Socket socket;

    /**
     * This is the main "brain" of the communications aspect of this application.
     *
     * @param model Handles the distribution of messages
     * @param view The GUI for the chat
     * @param socket The connection over the internet
     * @param out The text coming from the server to the GUI
     */
    public ChatController(ChatModel model, ChatView view, Socket socket, PrintWriter out) {
        this.model = model;
        this.view = view;
        this.socket = socket;
        this.out = out;

        view.setOnClose(this::shutdown);

        view.chatArea.sendButton.addActionListener(_ -> send());
        view.chatArea.textField.addActionListener(_ -> send());
    }

    /**
     * This takes the text from the text field and puts it into the main chat area
     */
    private void send() {
        String msg = view.chatArea.textField.getText();

        if (!msg.trim().isEmpty()) {
            out.println(msg);
            view.chatArea.textField.setText("");
        }
    }

    /**
     * This takes the text from the user and puts it into the text area
     * @param msg the message that you want to send over the network
     */
    public void receive(String message) {
        if (message.startsWith("USER_LIST:")) {
            String[] users = message.substring(10).split(",");
            SwingUtilities.invokeLater(() -> view.getChatArea().updateUsers(Arrays.asList(users)));
        } else {
            SwingUtilities.invokeLater(() -> view.getChatArea().appendMessage(message));
        }
    }

    /**
     * Closes the instance of client
     */
    public void shutdown() {
        try { if (socket != null && !socket.isClosed()) { socket.close(); } }
        catch (Exception ignored) {}
        System.exit(0);
    }
}