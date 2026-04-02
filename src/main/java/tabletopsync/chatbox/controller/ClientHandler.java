package tabletopsync.chatbox.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket socket;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket) { this.socket = socket; }

    public String getUsername() {
        return username;
    }

    /**
     * Prints the message onto the screen (view)
     * @param message the message the user sends
     */
    public void sendMessage(String message) { out.println(message); }

    /**
     * This method handles the code for the thread itself. This method specifically makes new users exist in the program.
     */
    @Override public void run() {
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
                if (message.startsWith("/")) { handleCommand(message); }
                else { ChatService.broadcast(username + ": " + message); }
            }
        }
        catch (IOException _) {}
        finally { disconnect();}
    }

    /**
     * Handles chat commands entered by the user.
     *
     * <p>Supported commands:</p>
     * <ul>
     *   <li><b>/users</b> – Displays a list of currently connected users.</li>
     *   <li><b>/quit</b> – Disconnects the client from the server.</li>
     * </ul>
     *
     * <p>If an unknown command is entered, an error message is returned to the user.</p>
     *
     * @param input the raw user input string beginning with a '/' command prefix
     *
     * @see ChatService#getUserList()
     */
    private void handleCommand(String input) {
        String[] parts = input.split(" ", 3);
        String cmd = parts[0];

        if (cmd.equals("/users")) { sendMessage(ChatService.getUserList()); }
        else if (cmd.equals("/quit"))
        {
            sendMessage("You have disconnected.");
            disconnect();
        }
        else { sendMessage("Unknown command"); }
    }

    /**
     * Disconnect from the server
     */
    public void disconnect() {
        try { socket.close(); }
        catch (IOException ignored) {}

        ChatService.removeClient(this);
        ChatService.broadcast(username + " left the chat");
    }
}