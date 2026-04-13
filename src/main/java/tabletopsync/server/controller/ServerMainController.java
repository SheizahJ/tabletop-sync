package tabletopsync.server.controller;

import tabletopsync.server.controller.ClientHandler;
import tabletopsync.server.view.ServerView;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServerMainController:
 * Main entry point for server logic
 *
 * READ------------------>
 * WHAT IT HANDLES:
 * - Starting the server socket
 * - Client connections
 * - Client threads
 * - Sending logs
 */
public class ServerMainController {

    private final ServerView view;
    private ServerSocket serverSocket;

    /**
     * Server controller constructor
     * @param view the server GUI for logging
     */
    public ServerMainController(ServerView view) {
        this.view = view;
    }

    /*--------------------------------
                  SERVER
     --------------------------------*/

    /**
     * Starts the server
     * @param port --> port #
     */
    public void startServer(int port) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                sendServerLog("Server running on port " + port);

                // Main loop: accept clients
                while (!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();
                    sendServerLog("Client connected: " + socket.getInetAddress());

                    // Spawn a dedicated thread for each client
                    ClientHandler clientHandler = new ClientHandler(socket);
                    clientHandler.start();
                }
            } catch (IOException e) {
                sendServerLog("Server error: " + e.getMessage());
            }
        }).start();
    }

    /*--------------------------------
                  LOGGING
     --------------------------------*/

    /**
     * Adds messages to the server GUI log
     * @param message the message to display
     */
    public void sendServerLog(String message) {
        SwingUtilities.invokeLater(() -> view.appendLog(message));
    }

    /*--------------------------------
                  SHUTDOWN
     --------------------------------*/

    /**
     * ***NEW added @teammates:
     * Shuts down the server and closes everything safely
     */
    public void shutdownServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                sendServerLog("Server shut down successfully.");
            }
        } catch (IOException e) {
            sendServerLog("Error shutting down server: " + e.getMessage());
        }
    }
}