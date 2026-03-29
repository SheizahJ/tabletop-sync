package controller;

import org.example.view.ChatDashboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {

    public static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    private static ChatDashboard dashboard;

    public static void setDashboard(ChatDashboard d) {
        dashboard = d;
    }

    public static void addClient(ClientHandler client) {
        String username = client.getUsername();

        if (username == null || username.isBlank()) return;

        clients.put(username, client);
        System.out.println(username + " has joined");

        if (dashboard != null) {
            dashboard.refresh();
        }
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client.getUsername());
        if (dashboard != null) {
            dashboard.refresh();
        }
        System.out.println(client.getUsername() + " has been removed");
    }

    public static void broadcast(String message) {
        for (ClientHandler client : clients.values()) { client.sendMessage(message); }
    }

    public static String getUserList() {
        return "Users: " + clients.keySet();
    }
}