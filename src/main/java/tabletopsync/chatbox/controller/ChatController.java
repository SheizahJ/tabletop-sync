package tabletopsync.chatbox.controller;

import tabletopsync.chatbox.model.ChatModel;
import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.service.PersistenceService;
import tabletopsync.chatbox.view.ChatView;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.sql.Time;
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
     * @param model Handles the distribution of messages
     * @param view The GUI for the chat
     * @param socket The connection over the internet
     * @param out The text coming from the server to the GUI
     */
    public ChatController(ChatModel model, ChatView view, Socket socket, PrintWriter out) {
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
            out.println(msg);
            view.chatArea.textField.setText("");
        }
    }

    /**
     * This takes the text from the user and puts it into the text area
     * MODIFICATION: Persistence in messages serverwide
     * @param message the message that you want to send over the network
     */
    public void receive(String message) {

        boolean isSystem = message.contains("joined") || message.contains("left") || message.contains("disconnected");

        if (message.startsWith("USER_LIST:")) {
            String[] users = message.substring(10).split(",");
            SwingUtilities.invokeLater(() -> view.getChatArea().updateUsers(Arrays.asList(users)));
        }
        else {
            SwingUtilities.invokeLater(() -> view.getChatArea().appendMessage(message));
        }

        // record the message onto JSON with metadata on who sent it and at what time
        String username = null;
        String messageContent = message;

        if (message.contains(":")) {
            String[] parts = message.split(":", 2);
            username = parts[0].trim();
            messageContent = parts[1].trim();
        }

        String type = isSystem ? "SYSTEM" : "CHAT";

        MessageObject object = new MessageObject(
                username,
                messageContent,
                new Time(System.currentTimeMillis()),
                type
        );

        if (isSystem) {
            PersistenceService.saveSystemMessage(object);
        } else {
            PersistenceService.saveChatMessage(object);
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