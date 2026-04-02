package tabletopsync.chatbox.controller;

import javax.swing.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages connected clients and message broadcasting.
 * Thread-safe using ConcurrentHashMap for client storage.
 */
public class ChatService {

<<<<<<< Updated upstream
    public static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>(); // thread-safe hashmap
=======
    public static final Map<String, ChatClientHandler> clients = new ConcurrentHashMap<>(); // thread-safe hashmap
>>>>>>> Stashed changes

    /**
     * Broadcasts the current list of connected users to all clients.
     *
     * <p>Constructs a message prefixed with {@code USER_LIST:} followed by a
     * comma-separated list of usernames, then sends it to every connected client.
     * Clients use this prefix to distinguish user list updates from chat messages.</p>
     *
     * <p>This method is called automatically whenever a user joins or leaves,
     * keeping all clients' user panels in sync.</p>
     *
     * <p>Example message format: {@code USER_LIST:alice,bob,charlie}</p>
     */
    public static void broadcastUserList() {
        String userListMessage = "USER_LIST:" + String.join(",", clients.keySet());
        broadcast(userListMessage);
    }

    /**
     * This adds client to the list of clients for the server to handle
     * @param client each instance of the application the user opens to connect to the server
     */
<<<<<<< Updated upstream
    public static void addClient(ClientHandler client) {
=======
    public static void addClient(ChatClientHandler client) {
>>>>>>> Stashed changes
        String username = client.getUsername();
        if (username == null || username.isBlank()) return;

        clients.put(username, client);
        System.out.println(username + " has joined");
        broadcastUserList();
    }

    /**
     * This removes the client from the list of clients for the server to handle
     * @param client each instance of the application the user opens to connect to the server
     */
<<<<<<< Updated upstream
    public static void removeClient(ClientHandler client) {
=======
    public static void removeClient(ChatClientHandler client) {
>>>>>>> Stashed changes
        clients.remove(client.getUsername());
        System.out.println(client.getUsername() + " has been removed");
        broadcastUserList();
    }

    /**
     * Takes the message and relays it to all users in the server
     * @param message the message being sent from a user
     */
    public static void broadcast(String message) {
<<<<<<< Updated upstream
        for (ClientHandler client : clients.values()) { client.sendMessage(message); }
=======
        for (ChatClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
>>>>>>> Stashed changes
    }

    /**
     * This returns the users in the client list
     * @return usernames
     */
    public static String getUserList() {
        return "Users: " + clients.keySet();
    }
}