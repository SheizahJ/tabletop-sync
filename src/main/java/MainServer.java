import tabletopsync.server.controller.ServerMainController;
import tabletopsync.server.view.ServerView;

import javax.swing.*;

/**
 * Entry point for the server application
 * - Launches server GUI
 * - Starts ServerMainController on defined port
 */
public class MainServer {
    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create server GUI
            ServerView serverView = new ServerView();

            // Create controller and start listening on port 8080
            ServerMainController controller = new ServerMainController(serverView);
            controller.startServer(8080);

            // Make GUI visible
            serverView.setVisible(true);
        });
    }
}