package tabletopsync.chatbox.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {

    public static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    /**
     * This adds client to the list of clients for the server to handle
     * @param client each instance of the application the user opens to connect to the server
     */
    public static void addClient(ClientHandler client) {
        String username = client.getUsername();

        if (username == null || username.isBlank()) return;

        clients.put(username, client);
        System.out.println(username + " has joined");

    }

    /**
     * This removes the client from the list of clients for the server to handle
     * @param client each instance of the application the user opens to connect to the server
     */
    public static void removeClient(ClientHandler client) {
        clients.remove(client.getUsername());
        System.out.println(client.getUsername() + " has been removed");
    }

    /**
     * Takes the message and relays it to all users in the server
     * @param message the message being sent from a user
     */
    public static void broadcast(String message) {
        for (ClientHandler client : clients.values()) { client.sendMessage(message); }
    }

    /**
     * This returns the users in the client list
     * @return usernames
     */
    public static String getUserList() {
        return "Users: " + clients.keySet();
    }
}