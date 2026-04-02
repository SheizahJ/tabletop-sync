import tabletopsync.chatbox.controller.ClientHandler;
import tabletopsync.server.view.ServerView;
import tabletopsync.server.controller.ServerController;
import tabletopsync.server.*;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

    public static void main(String[] args) {
        System.out.println("Server running on port 8080");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void startServer() {

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8888);
                log("Server running on port 8888");

                while (true) {
                    Socket socket = serverSocket.accept();
                    log("Client connected: " + socket.getInetAddress());
                    new Thread(new ClientHandler(socket)).start();
                }
            }
            catch (IOException e) { log("Server error: " + e.getMessage()); }
        }).start();
    }
}
