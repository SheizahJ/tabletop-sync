package tabletopsync.client.model.commands;

import tabletopsync.chatbox.controller.ChatController;
import tabletopsync.chatbox.view.ChatView;
import tabletopsync.server.model.ServerModel;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.Arrays;

public class ChatClientCmds {
    PrintWriter out;
    ChatController chatController;
    ChatView chatView;

    public ChatClientCmds(PrintWriter out, ChatController controller, ChatView chatView) {
        this.out = out;
        this.chatController = controller;
        this.chatView = chatView;
    }

    public void handleCommand(String message) {
        String[] parts = message.split(";");
        String command = parts[0];

        System.out.println(command);

        switch (command) {
            case "USER_LIST" -> {
                String[] users = message.substring(10).split(",");
                SwingUtilities.invokeLater(() ->
                        chatView.getChatArea().updateUsers(Arrays.asList(users))
                );
            }

            case "SYS_MSG" -> {
                String systemMessage = parts[1];
                SwingUtilities.invokeLater(() ->
                        chatView.getChatArea().appendMessage("SYSTEM", systemMessage, Color.GRAY)
                );
            }

            case "SEND_MSG" -> {
                // all other messages -> chat
                System.out.println(Arrays.toString(parts));
                String username = parts[1];
                String msg = parts[2].trim();
                String rgb = ServerModel.userColours.get(username);
                Color color = Color.WHITE; // fallback

                if (rgb != null) {
                    try {
                        String[] c = rgb.split(",");
                        color = new Color(
                                Integer.parseInt(c[0]),
                                Integer.parseInt(c[1]),
                                Integer.parseInt(c[2])
                        );
                    } catch (Exception ignored) {
                    }
                }

                Color finalColor = color;

                SwingUtilities.invokeLater(() ->
                        chatView.getChatArea().appendMessage(username, msg, finalColor)
                );
            }
        }
    }
}
