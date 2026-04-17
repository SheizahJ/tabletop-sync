import tabletopsync.client.controller.ClientController;

import javax.swing.*;

/**
 * Client side GUI
 * - Connects to server
 * - Sets up MVC chat and canvas
 * - Handles real-time canvas updates
 */

public class Client extends JFrame {
    static void main() {
        SwingUtilities.invokeLater(() -> new ClientController());
    }
}
