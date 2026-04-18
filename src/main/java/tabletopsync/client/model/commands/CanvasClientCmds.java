package tabletopsync.client.model.commands;

import tabletopsync.canvas.controller.CanvasController;
import tabletopsync.canvas.model.CanvasModel;
import tabletopsync.server.model.ServerModel;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.UUID;

public class CanvasClientCmds {
    PrintWriter out;
    CanvasController canvasController;
    CanvasModel canvasModel;

    public CanvasClientCmds(PrintWriter out, CanvasController controller, CanvasModel model) {
        this.out = out;
        this.canvasController = controller;
        this.canvasModel = model;
    }

    /**
     * Executes commands handled in the CLIENT. (Server > Client communication)
     * @param message The command message
     */
    public void handleCommand(String message) {
        String[] parts = message.split(";");
        String command = parts[0];

        // COMMANDS --------------------------------------
        switch (command) {
            case "INIT_CANVAS" -> {
                try {
                    // creates client-only sprites
                    if (parts.length == 1) {
                        // FOR TESTING PURPOSES: Creates default sprites
                        System.out.println("Adding Default Sprites...");
                        out.println("SPRITE_ADD;" + UUID.randomUUID() + ";/test_images/mario.png;0;0");
                        out.println("SPRITE_ADD;" + UUID.randomUUID() + ";/test_images/mario.png;0;0");
                        out.println("SPRITE_ADD;" + UUID.randomUUID() + ";/test_images/mario.png;0;0");
                    } else {
                        String[] sprites = parts[1].split(",");
                        for (String sprite : sprites) {
                            String[] spriteInfo = sprite.split(":");
                            System.out.println(Arrays.toString(spriteInfo));
                            String uuid = spriteInfo[0];
                            String resourcePath = spriteInfo[1];
                            int x = Integer.parseInt(spriteInfo[2]);
                            int y = Integer.parseInt(spriteInfo[3]);
                            SwingUtilities.invokeLater(() ->
                                    canvasController.addSpriteFromNetwork(resourcePath, uuid, x, y)
                            );
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Failed to initialize canvas..." + e.getMessage());
                }
            }
            case "SPRITE_MOVE" -> {
                try {
                    String uuid = parts[1];
                    int x = Integer.parseInt(parts[2]);
                    int y = Integer.parseInt(parts[3]);

                    // find sprite by uuid
                    var sprite = canvasModel.getSpriteByUuid(uuid);

                    if (sprite != null) {
                        if (sprite.getPosition().x != x || sprite.getPosition().y != y) {
                            sprite.move(x, y);
                            SwingUtilities.invokeLater(canvasController::repaint);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Invalid sprite move update: " + e.getMessage());
                }
            }
            case "SPRITE_ADD" -> {
                try {
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
                    System.err.println("Invalid sprite add update: " + e.getMessage());
                }
            }

            case "SPRITE_DELETE" -> {
                try {
                    String uuid = parts[1];

                    // Run on UI thread to prevent flickering or crashes
                    SwingUtilities.invokeLater(() -> {
                        canvasController.removeSpriteFromNetwork(uuid);
                    });
                } catch (Exception e) {
                    System.err.println("Invalid sprite delete update: " + e.getMessage());
                }
            }

            case "UPDATE_MAP" -> {
                try {
                    String resourcePath = parts[1];
                    int width = Integer.parseInt(parts[2]);
                    int height = Integer.parseInt(parts[3]);

                    SwingUtilities.invokeLater(() -> {
                        canvasController.setMap(resourcePath, width, height);
                    });
                }
                catch (Exception e) {
                    System.err.println("ERROR occurred while setting up client's map.. " + e.getMessage());
                }
            }
        }
    }
}
