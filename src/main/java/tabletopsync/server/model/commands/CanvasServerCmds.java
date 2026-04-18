package tabletopsync.server.model.commands;

import tabletopsync.server.controller.ClientHandler;
import tabletopsync.server.model.ServerModel;

import java.awt.*;

public class CanvasServerCmds {
    private final ClientHandler clientHandler;

    public CanvasServerCmds(ClientHandler client) {
        this.clientHandler = client;
    }

    /**
     * Executes commands handled in the SERVER. (Client > Server communication)
     * @param message The command message
     */
    public void handleCommand(String message) {
        String[] parts = message.split(";");
        String command = parts[0];

        System.out.println(command);

        // COMMANDS --------------------------------------
        switch (command) {
            // -------------------------------
            // Initializes the canvas requested by the client
            // format: INIT_CANVAS
            // -------------------------------
            case "INIT_CANVAS" -> {
                System.out.println("Attempting to initialize " + clientHandler.username + "'s canvas...");
                System.out.println("Sprites in Server: " + ServerModel.sprites);

                // Initialize Map
                if (ServerModel.map != null && !ServerModel.map.isEmpty()) {
                    // Just send the stored string as-is because it's already "UPDATE_MAP;path;w;h"
                    clientHandler.sendToClient(ServerModel.map);
                }

                // Initialize Sprites
                if (ServerModel.sprites.isEmpty()) {
                    clientHandler.sendToClient("INIT_CANVAS");
                } else {
                    String spriteData = String.join(",", ServerModel.sprites.values());
                    clientHandler.sendToClient("INIT_CANVAS;" + spriteData);
                }
            }

            // -------------------------------
            // Sprite move update from client
            // format: SPRITE_MOVE:uuid:x:y
            // -------------------------------
            case "SPRITE_MOVE" -> {
                try {
                    String uuid = parts[1];

                    int x = Integer.parseInt(parts[2]);
                    int y = Integer.parseInt(parts[3]);

                    // update Server Model's sprite data
                    String[] spriteData = ServerModel.sprites.get(uuid).split(":");
                    String resourcePath = spriteData[1];
                    ServerModel.sprites.put(uuid, uuid + ":" + resourcePath + ":" + x + ":" + y);

                    System.out.println(ServerModel.sprites.get(uuid));

                    // broadcast sprite movement to all clients
                    clientHandler.broadcast("SPRITE_MOVE;" + uuid + ";" + x + ";" + y);
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: Invalid sprite move format");
                }
            }

            // -------------------------------
            // Sprite add update from client
            // format: SPRITE_ADD:uuid:resourcePath:x:y
            // -------------------------------
            case "SPRITE_ADD" -> {
                try {
                    String uuid = parts[1];
                    String resourcePath = parts[2];
                    int x = Integer.parseInt(parts[3]);
                    int y = Integer.parseInt(parts[4]);

                    ServerModel.sprites.put(uuid, uuid + ":" + resourcePath + ":" + x + ":" + y);

                    // broadcast sprite creation to all clients
                    clientHandler.broadcast("SPRITE_ADD;" + uuid + ";" + resourcePath + ";" + x + ";" + y);
                } catch (Exception e) {
                    System.err.println("ERROR: Invalid sprite add format");
                }
            }

            case "SPRITE_DELETE" -> {
                try {
                    String uuid = parts[1];

                    // Remove from server memory so it doesn't reappear on INIT_CANVAS
                    ServerModel.sprites.remove(uuid);

                    // Tell everyone else to delete it
                    clientHandler.broadcast("SPRITE_DELETE;" + uuid);

                    System.out.println("Sprite " + uuid + " deleted by " + clientHandler.username);
                } catch (Exception e) {
                    System.err.println("ERROR: Invalid sprite delete format");
                }
            }

            case "UPDATE_MAP" -> {
                try {
                    String resourcePath = parts[1];
                    int width = Integer.parseInt(parts[2]);
                    int height = Integer.parseInt(parts[3]);

                    String toBroadcast = "UPDATE_MAP;" + resourcePath + ";" + width + ";" + height;
                    ServerModel.map = toBroadcast;

                    clientHandler.broadcast(toBroadcast);
                } catch (Exception e) {
                    System.err.println("ERROR occurred while updating the server's map: " + e.getMessage());
                }
            }

            case "SET_USER" -> {
                String username = parts[1];
                clientHandler.setUsername(username);

                // Register client
                clientHandler.registerClient();

                // broadcast the colours of the existing users
                // NOTE: there was an issue with colours not showing properly for later users so this is a quick fix.
                clientHandler.sendExistingColours();
            }

            case "COLOR_SELECT" -> {
                try {
                    String user = parts[1];
                    String[] colourArray = parts[2].split(",");

                    int r = Integer.parseInt(colourArray[0]);
                    int g = Integer.parseInt(colourArray[1]);
                    int b = Integer.parseInt(colourArray[2]);

                    Color color = new Color(r, g, b);
                    ServerModel.userColours.put(user, parts[2]); // store "r,g,b";

                } catch (Exception e) {
                    System.err.println("Invalid color broadcast: " + message);
                }
                // TODO: make comms over network: Sprite (add/delete from canvas, lock/unlock, see selected)
            }
        }
    }
}
