package tabletopsync.chatbox.controller;

import tabletopsync.chatbox.model.ChatModel;
<<<<<<< Updated upstream
=======
import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.controller.ServerMainController;
import tabletopsync.server.service.PersistenceService;
>>>>>>> Stashed changes
import tabletopsync.chatbox.view.ChatView;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

/**
 * This is the main "brain" of the communications aspect of this application.
 * - Sends and receives messages from the server
 * - Updates chat GUI
 * - Persists messages using PersistenceService
 */
public class ChatController {

    private final ChatView view;
    private final PrintWriter out;
<<<<<<< Updated upstream
    public Socket socket;
=======
    private final Socket socket;
>>>>>>> Stashed changes

    /**
     * Constructor
     *
     * @param model  Handles the distribution of messages
     * @param view   The GUI for the chat
     * @param socket The network connection
     * @param out    The writer for sending messages
     */
    public ChatController(ChatModel model, ChatView view, Socket socket, PrintWriter out) {
        this.view = view;
        this.socket = socket;
        this.out = out;

        view.setOnClose(this::shutdown);

        view.chatArea.sendButton.addActionListener(_ -> send());
        view.chatArea.textField.addActionListener(_ -> send());
<<<<<<< Updated upstream
=======

        // Load server-side chat history
        List<MessageObject> chatHistory = PersistenceService.loadChatMessages();
        List<MessageObject> systemHistory = PersistenceService.loadSystemMessages();

        SwingUtilities.invokeLater(() -> {
            for (MessageObject msg : systemHistory) view.getChatArea().appendMessage(msg.getMessage());
            for (MessageObject msg : chatHistory) {
                String formatted = msg.getUsername() + ": " + msg.getMessage();
                view.getChatArea().appendMessage(formatted);
            }
        });
>>>>>>> Stashed changes
    }

    /**
     * Sends the text from the input field to the server
     */
    private void send() {
        String msg = view.chatArea.textField.getText();
        if (!msg.trim().isEmpty()) {
            out.println(msg);
            view.chatArea.textField.setText("");
        }
    }

    /**
<<<<<<< Updated upstream
     * This takes the text from the user and puts it into the text area
     * @param message the message that you want to send over the network
     */
    public void receive(String message) {
=======
     * Handles messages received from the server
     * - Updates GUI
     * - Persists messages
     *
     * @param message the raw server message
     */
    public void receive(String message) {
        boolean isSystem = message.contains("joined") || message.contains("left") || message.contains("disconnected");

        // Update user list if message contains USER_LIST
>>>>>>> Stashed changes
        if (message.startsWith("USER_LIST:")) {
            String[] users = message.substring(10).split(",");
            SwingUtilities.invokeLater(() -> view.getChatArea().updateUsers(Arrays.asList(users)));
        } else {
            SwingUtilities.invokeLater(() -> view.getChatArea().appendMessage(message));
        }
<<<<<<< Updated upstream
=======

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

        // Save to server-side persistence
        if (isSystem) PersistenceService.saveSystemMessage(object);
        else PersistenceService.saveChatMessage(object);
>>>>>>> Stashed changes
    }

    /**
     * Shuts down the client
     */
    public void shutdown() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception ignored) {}
        System.exit(0);
    }
}