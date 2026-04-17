package tabletopsync.client.model.commands;

import tabletopsync.chatbox.controller.ChatController;

import java.io.PrintWriter;

public class ChatClientCmds {
    PrintWriter out;
    ChatController chatController;

    public ChatClientCmds(PrintWriter out, ChatController controller) {
        this.out = out;
        this.chatController = controller;
    }

    public void handleCommand(String message) {
        String[] parts = message.split(";");
        String command = parts[0];

        switch (command) {
            case "SEND_MSG" -> {
                // all other messages -> chat
                chatController.receive(parts[1]);
            }
        }
    }
}
