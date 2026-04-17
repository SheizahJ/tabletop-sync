package tabletopsync.chatbox.controller;

import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.service.PersistenceService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;

/**
 * Handles a single client connection on its own thread.
 * - Reads messages from the client
 * - Sends messages back to the client
 * - Updates ChatService
 * - Saves messages using PersistenceService
 */
public class ChatClientHandler extends Thread {

    private final Socket socket;
    private PrintWriter out;
    private String username;

    public ChatClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Prints the message onto the client screen
     *
     * @param message the message the user sends
     */
    public void sendMessage(String message) {
        if (out != null) out.println(message);
    }

    /**
     * This method handles the code for the thread itself.
     * This method specifically makes new users exist in the program.
     */
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out = new PrintWriter(socket.getOutputStream(), true);

            // Get username
            username = in.readLine();
            if (username == null || username.trim().isEmpty()) {
                socket.close();
                return;
            }

            // Register client
            ChatService.addClient(this);


            // Notify everyone
            ChatService.broadcast(username + " joined the chat");

            // Listen for messages
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/")) handleCommand(message);
                else {
                    ChatService.broadcast(username + ": " + message);

                    MessageObject msgObject = new MessageObject(
                            username,
                            message,
                            new Time(System.currentTimeMillis()),
                            "CHAT"
                    );

                    synchronized (ChatClientHandler.class) { // prevents race conditions
                        PersistenceService.saveChatMessage(msgObject);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Connection error with " + username + ": " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    /**
     * Handles chat commands entered by the user.
     */
    private void handleCommand(String input) {
        String[] parts = input.split(" ", 3);
        String cmd = parts[0];

        if (cmd.equals("/users")) sendMessage(ChatService.getUserList());
        else if (cmd.equals("/quit")) {
            sendMessage("You have disconnected.");
            disconnect();
        } else sendMessage("Unknown command");
    }

    /**
     * Disconnect from the server
     */
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
        ChatService.removeClient(this);
        ChatService.broadcast(username + " left the chat");
    }
}