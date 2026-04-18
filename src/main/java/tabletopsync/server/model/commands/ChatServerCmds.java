package tabletopsync.server.model.commands;

import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.controller.ClientHandler;
import tabletopsync.server.model.ServerModel;
import tabletopsync.server.service.PersistenceService;

import java.sql.Time;

public class ChatServerCmds {
    private final ClientHandler clientHandler;

    public ChatServerCmds(ClientHandler client) {
        this.clientHandler = client;
    }

    public void handleCommand(String message) {
        if (message.startsWith("/")) {
            if (message.equals("/users")) {
                clientHandler.sendToClient("Users: " + ServerModel.clients.keySet());
            } else if (message.equals("/quit")) {
                clientHandler.sendToClient("You have disconnected.");
                clientHandler.disconnect();
            } else {
                clientHandler.sendToClient("Unknown command");
            }
        }

        // -------------------------------
        // Regular chat message
        // -------------------------------
        else {
            String[] parts = message.split(";");
            String command = parts[0];

            switch (command) {
                case "SEND_MSG" -> {
                    String chatMessage = parts[1];
                    clientHandler.broadcast("SEND_MSG;" + clientHandler.username + ";" + chatMessage);

                    MessageObject chatMsg = new MessageObject(
                            clientHandler.username,
                            chatMessage,
                            new Time(System.currentTimeMillis()),
                            "CHAT"
                    );

                    if (ClientHandler.isNewMessage(chatMsg)) {
                        PersistenceService.saveChatMessage(chatMsg);
                    }
                }

                case "SYS_MSG" -> {
                    String systemMessage = parts[1];
                    clientHandler.broadcast("SYS_MSG;" + systemMessage);

                    MessageObject systemMsg = new MessageObject(
                            "SYSTEM",
                            systemMessage,
                            new Time(System.currentTimeMillis()),
                            "CHAT"
                    );

                    if (ClientHandler.isNewMessage(systemMsg)) {
                        PersistenceService.saveSystemMessage(systemMsg);
                    }
                }
                case "COLOR_SELECT" -> {
                    String rgb = parts[1];
                    ServerModel.userColours.put(clientHandler.username, rgb);
                    clientHandler.broadcast("COLOR_SELECT;" + clientHandler.username + ";" + rgb);
                }
            }
        }
    }
}
