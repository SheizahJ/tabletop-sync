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
import java.util.*;

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

            canvasController.setCanvasListener(new CanvasController.CanvasUpdateListener() {
                @Override
                public void onSpriteAdded(String uuid, String resourcePath, int x, int y) {
                    out.println("SPRITE_ADD:" + uuid + ":" + resourcePath + ":" + x + ":" + y);
                }

                @Override
                public void onSpriteMoved(String uuid, int x, int y) {
                    out.println("SPRITE_MOVE:" + uuid + ":" + x + ":" + y);
                }
            });

            // Initialize player's canvas
            out.println("INIT_CANVAS");


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

                        } else if(line.startsWith("INIT_CANVAS:")) {
                            try {
                                String[] parts = line.split(":");

                                // only initializes current client
                                String givenUser = parts[1];
                                if (givenUser.equals(username)) {
                                    // creates client-only sprites
                                    if (parts.length == 2) {
                                        // FOR TESTING PURPOSES: Creates default sprites
                                        System.out.println("Adding Default Sprites...");
                                        out.println("SPRITE_ADD:" + UUID.randomUUID() + ":/test_images/mario.png:0:0");
                                        out.println("SPRITE_ADD:" + UUID.randomUUID() + ":/test_images/mario.png:0:0");
                                        out.println("SPRITE_ADD:" + UUID.randomUUID() + ":/test_images/mario.png:0:0");
                                    } else {
                                        String[] sprites = parts[2].split(",");
                                        for (String sprite : sprites) {
                                            String[] spriteInfo = sprite.split(";");
                                            String uuid = spriteInfo[0];
                                            String resourcePath = spriteInfo[1];
                                            int x = Integer.parseInt(spriteInfo[2]);
                                            int y = Integer.parseInt(spriteInfo[3]);
                                            SwingUtilities.invokeLater(() ->
                                                    canvasController.addSpriteFromNetwork(resourcePath, uuid, x, y)
                                            );
                                        }
                                    }
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("Failed to initialize canvas...");
                            }
                        } else if (line.startsWith("SPRITE_MOVE:")) {
                            try {
                                String[] parts = line.split(":");
                                String uuid = parts[1];
                                int x = Integer.parseInt(parts[2]);
                                int y = Integer.parseInt(parts[3]);

                                // find sprite by uuid
                                System.out.println(uuid + ", " + x + ", " + y);
                                var sprite = canvasModel.getSpriteByUuid(uuid);

                                if (sprite != null) {
                                    sprite.move(x, y);

                                    SwingUtilities.invokeLater(canvasController::repaint);
                                }
                            } catch (Exception e) {
                                System.err.println("Invalid sprite move update: " + line);
                            }
                        } else if (line.startsWith("SPRITE_ADD:")) {
                            try {
                                String[] parts = line.split(":");
                                String uuid = parts[1];
                                String resourcePath = parts[2];
                                int x = Integer.parseInt(parts[3]);
                                int y = Integer.parseInt(parts[4]);

                                // only add if sprite does not already exist
                                if (canvasModel.getSpriteByUuid(uuid) == null) {
                                    SwingUtilities.invokeLater(() ->
                                            canvasController.addSpriteFromNetwork(resourcePath, uuid, x, y)
                                    );
                                }
                            } catch (Exception e) {
                                System.err.println("Invalid sprite add update: " + line);
                            }
                        }
                        else {
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
