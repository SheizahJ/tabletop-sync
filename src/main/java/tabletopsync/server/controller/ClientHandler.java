package tabletopsync.server.controller;

import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.model.commands.CanvasServerCmds;
import tabletopsync.server.model.commands.ChatServerCmds;
import tabletopsync.server.model.ServerModel;
import tabletopsync.server.service.PersistenceService;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles a single client connection on its own thread.
 * - reads messages from the client socket
 * - sends messages back to the client
 * - broadcasts messages to other clients
 * - persists chat and system messages
 * - handles safe disconnects
 */
public class ClientHandler extends Thread {
    private final CanvasServerCmds canvasCommands;
    private final ChatServerCmds chatCommands;
    private final Socket socket;
    private PrintWriter out;

    public String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        canvasCommands = new CanvasServerCmds(this);
        chatCommands = new ChatServerCmds(this);
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
    public String getUsername() { return username; }

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
     * Sends a message to this client.
     * @param message The message to send
     */
    public void sendToClient(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     * @param message The message to send
     */
    public void broadcast(String message) {
        for (ClientHandler client : ServerModel.clients.values()) {
            client.sendToClient(message);
        }
    }

    /**
     * Broadcasts a system message to all connected clients and persists it.
     *
     * @param message The system message
     */
    public void broadcastSystemMessage(String message) {
        MessageObject systemMsg = new MessageObject(
                null,
                message,
                new Time(System.currentTimeMillis()),
                "SYSTEM"
        );

        if (isNewMessage(systemMsg)) {
            PersistenceService.saveSystemMessage(systemMsg);
        }
        broadcast("SEND_MSG;" + message);
    }

    /**
     * Broadcasts a user list update to all clients
     */
    public void broadcastUserList() {
        String userListMessage = "USER_LIST;" + String.join(",", ServerModel.clients.keySet());
        broadcast(userListMessage);
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
                canvasCommands.handleCommand(message);
                chatCommands.handleCommand(message);
            }

        } catch (IOException e) {
            System.err.println("Connection error with " + username + ": " + e.getMessage());
        } finally {
            disconnect();
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
        } catch (IOException ignored) {}

        unregisterClient();
    }
}