import com.formdev.flatlaf.FlatLightLaf;
import tabletopsync.canvas.controller.CanvasController;
import tabletopsync.canvas.model.CanvasModel;
import tabletopsync.canvas.view.CanvasView;
import tabletopsync.chatbox.controller.ChatController;
import tabletopsync.chatbox.view.ChatView;
import tabletopsync.linker.TableTopGUI;
import tabletopsync.login.LoginModel;
import tabletopsync.login.LoginView;
import tabletopsync.server.model.ServerModel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

/**
 * Client side GUI
 * - Connects to server
 * - Sets up MVC chat and canvas
 * - Handles real-time canvas updates
 */

public class ClientMain extends JFrame {
    static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            Socket socket = new Socket("localhost", 8080);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            LoginModel loginModel = new LoginModel();
            LoginView loginView = new LoginView();
            loginView.setVisible(true);

            String username = loginView.getSubmittedUsername();
            Color colorSelection = loginView.getSubmittedColor();

            if (username == null) {
                System.exit(0);
            }
            if (colorSelection == null) {
                System.exit(0);
            }

            loginModel.setUsername(username);
            out.println(loginModel.getUsername()); // send username to server

            String rgb = colorSelection.getRed() + "," +
                    colorSelection.getGreen() + "," +
                    colorSelection.getBlue();

            out.println("COLOR_SELECT:" + rgb);

            // -----------------------
            // MVC setup
            // -----------------------
            ChatView chatView = new ChatView(loginModel.getUsername(), colorSelection);
            ChatController chatController = new ChatController(chatView, socket, out);

            CanvasModel canvasModel = new CanvasModel();

            // rami's stuff
            canvasModel.setUsername(loginModel.getUsername());

            CanvasController canvasController = new CanvasController(canvasModel, colorSelection);

            canvasController.setCanvasListener(new CanvasController.CanvasUpdateListener() {
                @Override
                public void onSpriteAdded(String uuid, String resourcePath, int x, int y) {
                    out.println("SPRITE_ADD:" + uuid + ":" + resourcePath + ":" + x + ":" + y);
                }

                // TODO: be able to globally remove a sprite and broadcast changes to sprite list on canvas

                @Override
                public void onSpriteMoved(String uuid, int x, int y) {
                    out.println("SPRITE_MOVE:" + uuid + ":" + x + ":" + y);
                }

                @Override
                public void onSpriteRemoved(String uuid) {
                    out.println("SPRITE_REMOVE:" + uuid);
                }
            });

            // Initialize player's canvas
            out.println("INIT_CANVAS");


            CanvasView canvasView = new CanvasView();
            canvasView.add(canvasController); // attach controller to view

            // TODO: reorient this to print to the server to update the location and broadcast it!!!!

            // attach network listener for canvas movement
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

                        } else if (line.startsWith("INIT_CANVAS:")) {
                            try {
                                String[] parts = line.split(":");

                                // only initializes current client
                                String givenUser = parts[1];
                                if (givenUser.equals(loginModel.getUsername())) {
                                    // creates client-only sprites
                                    if (parts.length == 2) {
                                        // FOR TESTING PURPOSES: Creates default sprites
                                        System.out.println("Adding Default Sprites...");
                                        out.println("SPRITE_ADD:" + UUID.randomUUID() + ":/test_images/mario.png:0:0");
                                        out.println("SPRITE_ADD:" + UUID.randomUUID() + ":/test_images/mario.png:100:100");
                                        out.println("SPRITE_ADD:" + UUID.randomUUID() + ":/test_images/mario.png:200:200");
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
                                // TODO: broadcast changes to sprite list and position on canvas
                                String[] parts = line.split(":");
                                String uuid = parts[1];
                                String resourcePath = parts[2];
                                int x = Integer.parseInt(parts[3]);
                                int y = Integer.parseInt(parts[4]);

                                SwingUtilities.invokeLater(() -> {
                                    canvasController.addSpriteFromNetwork(resourcePath, uuid, x, y);
                                    }
                                );

                            } catch (Exception e) {
                                System.err.println("Invalid sprite add update: " + line);
                            }
                        }
                        // TODO: be able to globally remove a sprite and broadcast changes to sprite list on canvas
                        else if (line.startsWith("SPRITE_REMOVE:")) {
                            try {
                                String[] parts = line.split(":");
                                String uuid = parts[1];
                                String resourcePath = parts[2];
                                int x = Integer.parseInt(parts[3]);
                                int y = Integer.parseInt(parts[4]);

                                // TODO: check existence of sprite, then remove globally
                                SwingUtilities.invokeLater(() -> {
                                            canvasController.removeSpriteFromNetwork(uuid);
                                        }
                                );

                            }
                            catch (NumberFormatException e) {
                                System.err.println("Failed to remove sprite...");
                            }
                        } else if (line.startsWith("COLOR_SELECT:")) {
                            try {
                                String[] parts = line.split(":");

                                String user = parts[1];
                                String[] colourArray = parts[2].split(",");

                                int r = Integer.parseInt(colourArray[0]);
                                int g = Integer.parseInt(colourArray[1]);
                                int b = Integer.parseInt(colourArray[2]);

                                Color color = new Color(r, g, b);
                                ServerModel.userColours.put(user, parts[2]); // store "r,g,b";

                            } catch (Exception e) {
                                System.err.println("Invalid color broadcast: " + line);
                            }
                            // TODO: make comms over network: Sprite (add/delete from canvas, lock/unlock, see selected)
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
