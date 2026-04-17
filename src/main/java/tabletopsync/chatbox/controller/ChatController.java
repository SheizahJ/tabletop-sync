package tabletopsync.chatbox.controller;

import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.chatbox.view.ChatView;
import tabletopsync.login.LoginView;
import tabletopsync.server.model.ServerModel;
import tabletopsync.server.service.PersistenceService;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * This is the main "brain" of the communications aspect of this application.
 * <p>
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

    private LoginView loginView;

    /**
     * This is the main "brain" of the communications aspect of this application.
     *
     * @param view   The GUI for the chat
     * @param socket The connection over the internet
     * @param out    The text coming from the server to the GUI
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
            for (MessageObject msg : systemHistory) {
                view.getChatArea().appendMessage(
                        "SYSTEM",
                        msg.getMessage(),
                        Color.GRAY
                );
            }

            for (MessageObject msg : chatHistory) {

                String username = msg.getUsername();
                String message = msg.getMessage();

                String rgb = ServerModel.userColours.get(username);

                Color color = Color.WHITE; // fallback colour

                if (rgb != null) {
                    try {
                        String[] parts = rgb.split(",");
                        color = new Color(
                                Integer.parseInt(parts[0]),
                                Integer.parseInt(parts[1]),
                                Integer.parseInt(parts[2])
                        );
                    } catch (Exception ignored) {
                    }
                }

                view.getChatArea().appendMessage(username, message, color);
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
     *
     * @param message the message that you want to send over the network
     */
    public void receive(String message) {

        if (message.startsWith("USER_LIST")) {
            String[] users = message.substring(10).split(",");
            SwingUtilities.invokeLater(() ->
                    view.getChatArea().updateUsers(Arrays.asList(users))
            );
            return;
        }

        if (!message.contains(":")) {
            SwingUtilities.invokeLater(() ->
                    view.getChatArea().appendMessage("SYSTEM", message, Color.GRAY)
            );
            return;
        }


        String[] parts = message.split(":", 2);
        String username = parts[0];
        String msg = parts[1].trim();

        String rgb = ServerModel.userColours.get(username);

        Color color = Color.WHITE; // fallback

        if (rgb != null) {
            try {
                String[] c = rgb.split(",");
                color = new Color(
                        Integer.parseInt(c[0]),
                        Integer.parseInt(c[1]),
                        Integer.parseInt(c[2])
                );
            } catch (Exception ignored) {
            }
        }

        Color finalColor = color;

        SwingUtilities.invokeLater(() ->
                view.getChatArea().appendMessage(username, msg, finalColor)
        );
    }

    /**
     * Closes the instance of client
     */
    public void shutdown() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception ignored) {
        }
        System.exit(0);
    }
}