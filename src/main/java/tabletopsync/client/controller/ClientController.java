package tabletopsync.client.controller;

import tabletopsync.client.model.commands.CanvasClientCmds;
import tabletopsync.client.model.commands.ChatClientCmds;
import tabletopsync.client.model.ClientModel;
import tabletopsync.client.view.ClientView;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientController {
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    CanvasClientCmds canvasCommands;
    ChatClientCmds chatCommands;

    public ClientController() {
        try {
            this.socket = new Socket("localhost", 8080);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // get player's username
            String username = JOptionPane.showInputDialog("Enter username:");
            if (username == null || username.trim().isEmpty()) return;
            out.println(username);


            // Initialize client
            ClientModel clientModel = new ClientModel(socket, out, username);
            out.println("INIT_CANVAS");

            SwingUtilities.invokeLater(() -> new ClientView(clientModel.chatView, clientModel.canvasView));


            // Set up commands
            this.canvasCommands = new CanvasClientCmds(out, clientModel.canvasController, clientModel.canvasModel);
            this.chatCommands = new ChatClientCmds(out, clientModel.chatController);

            // -----------------------
            // listen for messages
            // -----------------------
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        canvasCommands.handleCommand(message);
                        chatCommands.handleCommand(message);
                    }
                } catch (IOException e) {
                    System.err.println("ERROR occurred while initializing ClientMain" + e.getMessage());
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // note: maintains original structure and comments
    }
}
