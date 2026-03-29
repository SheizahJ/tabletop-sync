import controller.ChatController;
import model.ChatModel;
import view.ChatView;
import com.formdev.flatlaf.FlatDarkLaf;

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