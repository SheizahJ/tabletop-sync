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
            }
            else if (message.equals("/quit")) {
                clientHandler.sendToClient("You have disconnected.");
                clientHandler.disconnect();
            }
            else {
                clientHandler.sendToClient("Unknown command");
            }
        }

        // -------------------------------
        // Regular chat message
        // -------------------------------
        else {
            String[] parts = message.split(";");
            String command = parts[0];

            if (command.equals("SEND_MSG")) {
                String formatted = clientHandler.username + ": " + parts[1];
                clientHandler.broadcast("SEND_MSG;" + formatted);

                MessageObject chatMsg = new MessageObject(
                        clientHandler.username,
                        message,
                        new Time(System.currentTimeMillis()),
                        "CHAT"
                );

                if (ClientHandler.isNewMessage(chatMsg)) {
                    PersistenceService.saveChatMessage(chatMsg);
                }
            }
        }
    }
}
