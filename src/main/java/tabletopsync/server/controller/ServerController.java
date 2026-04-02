package tabletopsync.server.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import tabletopsync.chatbox.controller.ClientViewHandler;
import tabletopsync.chatbox.model.MessageObject;
import tabletopsync.server.model.ServerModel;
import tabletopsync.server.view.ServerView;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 */
public class ServerController {

    static ServerModel model = new ServerModel();
    private ServerView view;

    private Socket socket = model.socket;
    private static final String HOST = ServerModel.HOST;
    private static final int PORT = ServerModel.PORT;
    private BufferedReader in;
    private PrintWriter out;


    /**
     * Server controller constructor
     * @param view
     */
    public ServerController(ServerView view) {this.view = view;}

    /**
     * Opens a fresh socket connection for a single request.
     * This keeps each operation simple: connect, do one job, then close.
     */
    public void openConnection() throws IOException {
        System.out.println("Opening connection.");
        socket = new Socket(HOST, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Closes all connection resources safely after each request.
     */
    public void closeConnection() {
        System.out.println("Connection closed.");
        try { if (in != null) { in.close(); } }
        catch (IOException e) { e.printStackTrace(); }
        if (out != null) { out.close(); }
        try { if (socket != null && !socket.isClosed()) { socket.close(); } }
        catch (IOException e) { e.printStackTrace(); }
    }


    /*--------------------------------
                   CHAT
     --------------------------------*/

    // Chat controller persistence read
    public List<MessageObject> getChatHistory() {
        return ServerModel.loadChatMessages();
    }
    public List<MessageObject> getSystemHistory() {
        return ServerModel.loadSystemMessages();
    }
    // chat controller persistence write
    public static void sendChatMessage(MessageObject message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendSystemMessage(MessageObject message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*--------------------------------
                  SERVER
     --------------------------------*/

    public void startServer() {

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                view.appendLog("Server running on port 8080");

                while (true) {
                    Socket socket = serverSocket.accept();
                    view.appendLog("Client connected: " + socket.getInetAddress());

                    // TODO: retreive client username, color, details/etc.

                    new Thread(new ClientViewHandler(socket)).start();
                }
            }
            catch (IOException e) {
                view.appendLog("Server error: " + e.getMessage());
            }
        }).start();
    }


}
