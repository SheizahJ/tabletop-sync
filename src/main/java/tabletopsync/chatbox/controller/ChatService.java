package tabletopsync.chatbox.controller;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages all chat clients connected to the server
 */
public class ChatService {

    private static final Set<ChatClientHandler> clients = ConcurrentHashMap.newKeySet();

    public static void addClient(ChatClientHandler client) { clients.add(client); }
    public static void removeClient(ChatClientHandler client) { clients.remove(client); }

    public static void broadcast(String message) {
        for (ChatClientHandler c : clients) c.sendMessage(message);
    }

    public static String getUserList() {
        StringBuilder sb = new StringBuilder();
        for (ChatClientHandler c : clients) {
            if (c.getUsername() != null) sb.append(c.getUsername()).append(", ");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "";
    }
}