package tabletopsync.server.controller;

import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.service.PersistenceService;
import java.io.*;
import java.net.Socket;
import java.sql.Time;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles a single client connection on its own thread.
 * - reads messages from the client socket
 * - sends messages back to the client
 * - broadcasts messages to other clients
 * - persists chat and system messages
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private PrintWriter out;
    private String username;
    private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public ClientHandler(Socket socket) { this.socket = socket; }
    public String getUsername() { return username; }

    public void sendMessage(String message) { if (out != null) out.println(message); }

    private void registerClient() {
        if (username != null && !username.isBlank()) {
            clients.put(username, this);
            broadcastUserList();
            broadcastSystemMessage(username + " joined the chat");
        }
    }

    private void unregisterClient() {
        if (username != null) {
            clients.remove(username);
            broadcastUserList();
            broadcastSystemMessage(username + " left the chat");
        }
    }

    private static void broadcast(String message) {
        for (ClientHandler client : clients.values()) client.sendMessage(message);
    }

    private static void broadcastSystemMessage(String message) {
        MessageObject sysMsg = new MessageObject(null, message, new Time(System.currentTimeMillis()), "SYSTEM");
        PersistenceService.saveSystemMessage(sysMsg);
        broadcast(message);
    }

    private static void broadcastUserList() {
        String userListMessage = "USER_LIST:" + String.join(",", clients.keySet());
        broadcast(userListMessage);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);

            username = in.readLine();
            if (username == null || username.trim().isEmpty()) { socket.close(); return; }

            registerClient();

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/")) handleCommand(message);
                else {
                    String formatted = username + ": " + message;
                    broadcast(formatted);

                    MessageObject chatMsg = new MessageObject(username, message, new Time(System.currentTimeMillis()), "CHAT");
                    PersistenceService.saveChatMessage(chatMsg);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection error with " + username + ": " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void handleCommand(String input) {
        String[] parts = input.split(" ", 2);
        String cmd = parts[0];

        if (cmd.equals("/users")) sendMessage("Users: " + clients.keySet());
        else if (cmd.equals("/quit")) { sendMessage("You have disconnected."); disconnect(); }
        else sendMessage("Unknown command");
    }

    public void disconnect() {
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
        unregisterClient();
    }
}