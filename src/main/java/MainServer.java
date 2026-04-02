import tabletopsync.chatbox.controller.ClientHandler;
import tabletopsync.server.view.ServerView;
import tabletopsync.server.controller.ServerController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static tabletopsync.server.view.ServerView.log;

public class MainServer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerView serverView = new ServerView();
            startServer();
            serverView.setVisible(true);
        });
    }
    private static void startServer() {

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                log("Server running on port 8080");

                while (true) {
                    Socket socket = serverSocket.accept();
                    log("Client connected: " + socket.getInetAddress());

                    // TODO: retreive client username, color, details/etc.

                    new Thread(new ClientHandler(socket)).start();
                }
            }
            catch (IOException e) { log("Server error: " + e.getMessage()); }
        }).start();
    }
}
