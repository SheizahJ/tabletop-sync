<<<<<<< Updated upstream
import tabletopsync.chatbox.controller.ClientHandler;
=======
import tabletopsync.server.view.ServerView;
import tabletopsync.server.controller.ServerMainController;
>>>>>>> Stashed changes

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

<<<<<<< Updated upstream
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
=======
void main() {
    SwingUtilities.invokeLater(() -> {
        ServerView serverView = new ServerView();
        ServerMainController controller = new ServerMainController(serverView);
        controller.startServer(8080);
        serverView.setVisible(true);
    });
>>>>>>> Stashed changes
}
