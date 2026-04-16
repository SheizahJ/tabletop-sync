package tabletopsync.server.controller;

import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.model.ServerModel;
import tabletopsync.server.service.PersistenceService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles a single client connection on its own thread.
 * - reads messages from the client socket
 * - sends messages back to the client
 * - broadcasts messages to other clients
 * - persists chat and system messages
 * - handles safe disconnects
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    // TO HANDLE DUPLICATE ENTRIES ON LOAD - rami
    private static boolean isNewMessage(MessageObject message) {
        List<MessageObject> existingMessages = new ArrayList<>();
        existingMessages.addAll(PersistenceService.loadChatMessages());
        existingMessages.addAll(PersistenceService.loadSystemMessages());

        return existingMessages.stream().noneMatch(msg ->
                msg.getUsername() != null &&
                        msg.getUsername().equals(message.getUsername()) &&
                        msg.getMessage().equals(message.getMessage())
        );
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message The message to send
     */
    private static void broadcast(String message) {
        for (ClientHandler client : ServerModel.clients.values()) {
            client.sendMessage(message);
        }
    }

    /**
     * Broadcasts a system message to all connected clients and persists it.
     *
     * @param message The system message
     */
    private static void broadcastSystemMessage(String message) {
        MessageObject systemMsg = new MessageObject(
                null,
                message,
                new Time(System.currentTimeMillis()),
                "SYSTEM"
        );

        if (isNewMessage(systemMsg)) {
            PersistenceService.saveSystemMessage(systemMsg);
        }
        broadcast(message);
    }

    /**
     * Broadcasts a user list update to all clients
     */
    private static void broadcastUserList() {
        String userListMessage = "USER_LIST:" + String.join(",", ServerModel.clients.keySet());
        broadcast(userListMessage);
    }

    public String getUsername() {
        return username;
    }

    /**
     * Sends a message to this client.
     *
     * @param message The message to send
     */
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    /**
     * Registers the client and broadcasts a system message.
     */
    private void registerClient() {
        if (username != null && !username.isBlank()) {
            ServerModel.clients.put(username, this);
            broadcastUserList();
            broadcastSystemMessage(username + " joined the chat");
        }
    }

    private void sendExistingColours() {
        for (String user : ServerModel.userColours.keySet()) {
            String rgb = ServerModel.userColours.get(user);
            sendMessage("COLOR_SELECT:" + user + ":" + rgb);
        }
    }

    /**
     * Unregisters the client and broadcasts a system message.
     */
    private void unregisterClient() {
        if (username != null) {
            ServerModel.clients.remove(username);
            broadcastUserList();
            broadcastSystemMessage(username + " left the chat");
        }
    }

    /**
     * Main thread method.
     * Handles username assignment, message reading, chat and canvas updates.
     */
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);

            // Get username from client
            username = in.readLine();

            if (username == null || username.trim().isEmpty()) {
                socket.close();
                return;
            }

            // Register client
            registerClient();

            // broadcast the colours of the existing users
            // NOTE: there was an issue with colours not showing properly for later users so this is a quick fix.
            sendExistingColours();

            // Listen for messages
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/")) {
                    handleCommand(message);
                }
                // -------------------------------
                // Canvas update from client
                // format: CANVAS:x,y
                // -------------------------------
                else if (message.startsWith("CANVAS:")) {
                    try {
                        String[] parts = message.substring(7).split(",");
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);

                        // broadcast to all clients with username
                        broadcast("CANVAS:" + username + ":" + x + "," + y);
                    } catch (Exception e) {
                        sendMessage("ERROR: Invalid canvas update format");
                    }
                }
                // -------------------------------
                // Initializes the canvas requested by the client
                // format: INIT_CANVAS
                // -------------------------------
                else if (message.startsWith("INIT_CANVAS")) {
                    System.out.println("Attempting to initialize " + username + "'s canvas...");
                    System.out.println("Sprites in Server: " + ServerModel.sprites);
                    if (ServerModel.sprites.isEmpty()) {
                        broadcast("INIT_CANVAS:" + username);
                    } else {
                        String spriteData = String.join(",", ServerModel.sprites.values());
                        broadcast("INIT_CANVAS:" + username + ":" + spriteData);
                    }
                }
                // -------------------------------
                // Sprite move update from client
                // format: SPRITE_MOVE:uuid:x:y
                // -------------------------------
                else if (message.startsWith("SPRITE_MOVE:")) {
                    try {
                        String[] parts = message.split(":");
                        String uuid = parts[1];

                        int x = Integer.parseInt(parts[2]);
                        int y = Integer.parseInt(parts[3]);

                        // update Server Model's sprite data
                        String[] spriteData = ServerModel.sprites.get(uuid).split(";");
                        String resourcePath = spriteData[1];
                        ServerModel.sprites.put(uuid, uuid + ";" + resourcePath + ";" + x + ";" + y);

//                        System.out.println(uuid + ", " + x + ", " + y);

                        // broadcast sprite movement to all clients
                        broadcast("SPRITE_MOVE:" + uuid + ":" + x + ":" + y);
                    } catch (NumberFormatException e) {
                        sendMessage("ERROR: Invalid sprite move format");
                    }
                }
                // -------------------------------
                // Sprite add update from client
                // format: SPRITE_ADD:uuid:resourcePath:x:y
                // -------------------------------
                else if (message.startsWith("SPRITE_ADD:")) {
                    try {
                        String[] parts = message.split(":");
                        String uuid = parts[1];
                        String resourcePath = parts[2];
                        int x = Integer.parseInt(parts[3]);
                        int y = Integer.parseInt(parts[4]);

                        ServerModel.sprites.put(uuid, uuid + ";" + resourcePath + ";" + x + ";" + y);

                        // broadcast sprite creation to all clients
                        broadcast("SPRITE_ADD:" + uuid + ":" + resourcePath + ":" + x + ":" + y);
                    } catch (Exception e) {
                        sendMessage("ERROR: Invalid sprite add format");
                    }
                } else if (message.startsWith("COLOR_SELECT:")) {
                    try {
                        String RGB = message.substring("COLOR_SELECT:".length());
                        ServerModel.userColours.put(username, RGB);
                        broadcast("COLOR_SELECT:" + username + ":" + RGB);
                    } catch (Exception e) {
                        sendMessage("ERROR: Invalid color format");
                    }
                }
                // -------------------------------
                // Regular chat message
                // -------------------------------
                else {
                    String formatted = username + ": " + message;
                    broadcast(formatted);

                    MessageObject chatMsg = new MessageObject(
                            username,
                            message,
                            new Time(System.currentTimeMillis()),
                            "CHAT"
                    );
                    if (isNewMessage(chatMsg)) {
                        PersistenceService.saveChatMessage(chatMsg);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Connection error with " + username + ": " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    /**
     * Handles client commands such as /quit or /users
     *
     * @param input raw input starting with /
     */
    private void handleCommand(String input) {
        String[] parts = input.split(" ", 2);
        String cmd = parts[0];

        if (cmd.equals("/users")) {
            sendMessage("Users: " + ServerModel.clients.keySet());
        } else if (cmd.equals("/quit")) {
            sendMessage("You have disconnected.");
            disconnect();
        } else {
            sendMessage("Unknown command");
        }
    }

    /**
     * Disconnects the client safely
     */
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }

        unregisterClient();
    }
}