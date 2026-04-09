import tabletopsync.canvas.controller.CanvasController;
import tabletopsync.canvas.model.CanvasModel;
import tabletopsync.canvas.view.CanvasView;
import tabletopsync.chatbox.controller.ChatController;
import tabletopsync.chatbox.model.ChatModel;
import tabletopsync.chatbox.view.ChatView;
import tabletopsync.linker.TableTopGUI;
import tabletopsync.login.LoginView;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * Client side GUI
 * - Connects to server
 * - Sets up MVC chat and canvas
 * - Handles real-time canvas updates
 */

public class ClientMain extends JFrame {
    void main() {
        try {
            Socket socket = new Socket("localhost", 8080);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // TODO: replace this with proper GUI
            // ask username
//            LoginView loginView = new LoginView();
//            loginView.setVisible(true);
            String username = JOptionPane.showInputDialog("Enter username:");
            if (username == null || username.trim().isEmpty()) return;

            out.println(username); // send username to server

            // -----------------------
            // MVC setup
            // -----------------------
            ChatModel chatModel = new ChatModel();
            ChatView chatView = new ChatView(username);
            ChatController chatController = new ChatController(chatView, socket, out);

            CanvasModel canvasModel = new CanvasModel();

            // rami's stuff
            canvasModel.setUsername(username);

            CanvasController canvasController = new CanvasController(canvasModel);
            CanvasView canvasView = new CanvasView();
            canvasView.add(canvasController); // attach controller to view

            // TODO: reorient this to print to the server to update the location and broadcast it!!!!

//        // attach network listener for canvas movement
//        canvasController.setNetworkListener((x, y) -> out.println("CANVAS:" + x + "," + y));

            SwingUtilities.invokeLater(() -> new TableTopGUI(chatView, canvasView));

            // -----------------------
            // listen for messages
            // -----------------------
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {

                        // check for canvas updates from server
                        if (line.startsWith("CANVAS:")) {
                            // format: CANVAS:x,y
                            String[] coords = line.substring(7).split(",");
                            int x = Integer.parseInt(coords[0]);
                            int y = Integer.parseInt(coords[1]);

                            // update model (repaints automatically via listener)
                            canvasModel.setCamera(x, y);

                        } else {
                            // all other messages -> chat
                            chatController.receive(line);
                        }
                    }
                } catch (IOException ignored) {
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // note: maintains original structure and comments
    }
}
