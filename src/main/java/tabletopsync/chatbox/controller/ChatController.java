package tabletopsync.chatbox.controller;

import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.service.PersistenceService;
import tabletopsync.chatbox.view.ChatView;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * This is the main "brain" of the communications aspect of this application.
 *
 * Handles:
 * - Sending messages from client to server
 * - Receiving messages from server
 * - Updating chat GUI
 * - Persisting messages using server-side persistence
 */
public class ChatController {

    private final ChatView view;
    private final PrintWriter out;
    public Socket socket;

    /**
     * This is the main "brain" of the communications aspect of this application.
     *
     * @param view The GUI for the chat
     * @param socket The connection over the internet
     * @param out The text coming from the server to the GUI
     */
    public ChatController(ChatView view, Socket socket, PrintWriter out) {
        this.view = view;
        this.socket = socket;
        this.out = out;

        view.setOnClose(this::shutdown);

        view.chatArea.sendButton.addActionListener(_ -> send());
        view.chatArea.textField.addActionListener(_ -> send());

        // Load server-side chat history
        List<MessageObject> chatHistory = PersistenceService.loadChatMessages();
        List<MessageObject> systemHistory = PersistenceService.loadSystemMessages();

        SwingUtilities.invokeLater(() -> {
            // Load system logs
            for (MessageObject msg : systemHistory) {
                view.getChatArea().appendMessage(msg.getMessage());
            }

            // Load chat messages
            for (MessageObject msg : chatHistory) {
                String formatted = msg.getUsername() + ": " + msg.getMessage();
                view.getChatArea().appendMessage(formatted);
            }
        });
    }

    /**
     * This takes the text from the text field and puts it into the main chat area
     */
    private void send() {
        String msg = view.chatArea.textField.getText();

        if (!msg.trim().isEmpty()) {
            out.println("SEND_MSG;" + msg);
            view.chatArea.textField.setText("");
        }
    }

    /**
     * This takes the text from the user and puts it into the text area
     * MODIFICATION: Persistence in messages serverwide
     * @param message the message that you want to send over the network
     */
    public void receive(String message) {

        if (message.startsWith("USER_LIST")) {
            String[] users = message.substring(10).split(",");
            SwingUtilities.invokeLater(() -> view.getChatArea().updateUsers(Arrays.asList(users)));
        }
        else {
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