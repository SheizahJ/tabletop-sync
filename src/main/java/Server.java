import tabletopsync.server.view.ServerView;
import tabletopsync.server.controller.ServerController;

import javax.swing.*;

/**
 * Entry point for the server application
 * - Launches server GUI
 * - Starts ServerMainController on defined port
 */
public class Server {
    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create server GUI
            ServerView serverView = new ServerView();

            // Create controller and start listening on port 8080
            ServerController controller = new ServerController(serverView);
            controller.startServer(8080);

            // Make GUI visible
            serverView.setVisible(true);
        });
    }
}