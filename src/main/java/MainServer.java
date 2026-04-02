import tabletopsync.server.view.ServerView;
import tabletopsync.server.controller.ServerController;

import javax.swing.*;

void main() {
    SwingUtilities.invokeLater(() -> {
        ServerView serverView = new ServerView();
        ServerController controller = new ServerController(serverView);
        controller.startServer();
        serverView.setVisible(true);
    });
}
