package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket socket;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message) { out.println(message); }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Get username
            username = in.readLine();

            if (username == null || username.trim().isEmpty()) {
                socket.close();
                return;
            }

            // Register client
            ChatService.addClient(this);

            // Notify everyone
            ChatService.broadcast(username + " joined the chat");

            // Listen for messages
            String message;
            while ((message = in.readLine()) != null) {

                // Commands
                if (message.startsWith("/")) {
                    handleCommand(message);
                } else {
                    ChatService.broadcast(username + ": " + message);
                }
            }

        }
        catch (IOException _) {}
        finally { disconnect();}
    }

    private void handleCommand(String input) {
        String[] parts = input.split(" ", 3);
        String cmd = parts[0];

        if (cmd.equals("/users")) {
            sendMessage(ChatService.getUserList());
        }
        else if (cmd.equals("/quit")) {
            sendMessage("You have disconnected.");
            disconnect();
        }
        else {
            sendMessage("Unknown command");
        }
    }

    public void disconnect() {
        try { socket.close(); }
        catch (IOException ignored) {}

        ChatService.removeClient(this);
        ChatService.broadcast(username + " left the chat");
    }
}