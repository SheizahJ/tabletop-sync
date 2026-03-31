import tabletopsync.chatbox.model.ChatModel;
import tabletopsync.chatbox.view.ChatView;
import tabletopsync.chatbox.controller.ChatController;
import tabletopsync.canvas.model.CanvasModel;
import tabletopsync.canvas.view.CanvasView;
import tabletopsync.canvas.controller.CanvasController;
import tabletopsync.linker.TableTopGUI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientApp {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // ask username
            String username = JOptionPane.showInputDialog("Enter username:");
            if (username == null || username.trim().isEmpty()) return;

            out.println(username);

            // MVC setup
            ChatModel model = new ChatModel();
            ChatView view = new ChatView(username);
            ChatController controller = new ChatController(model, view, socket, out);

            CanvasModel canvasModel = new CanvasModel();
            CanvasController canvasController = new CanvasController(canvasModel);
            CanvasView canvasView = new CanvasView();

            SwingUtilities.invokeLater(() -> {
                canvasView.add(canvasController);
                new TableTopGUI(view, canvasView);
            });

            // listen for messages
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        controller.receive(line);
                    }
                } catch (IOException ignored) {}
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}