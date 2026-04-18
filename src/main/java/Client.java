import tabletopsync.client.controller.ClientController;
import tabletopsync.login.LoginModel;
import tabletopsync.login.LoginView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Client side GUI
 * - Connects to server
 * - Sets up MVC chat and canvas
 * - Handles real-time canvas updates
 */

public class Client extends JFrame {
    static void main() {
        try {
            // Show Login Page
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            LoginModel loginModel = new LoginModel();
            String username = loginView.getSubmittedUsername();
            Color colorSelection = loginView.getSubmittedColor();

            if (username == null) {
                System.exit(0);
            }
            if (colorSelection == null) {
                System.exit(0);
            }

            loginModel.setUsername(username);

            //
            SwingUtilities.invokeLater(() -> new ClientController(loginView, loginModel));
        } catch (IOException e) {
            System.err.println("ERROR while logging in...");
        }
    }
}
